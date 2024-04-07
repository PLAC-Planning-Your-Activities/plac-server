package com.plac.domain.plan.service;

import com.plac.domain.destination.entity.Destination;
import com.plac.domain.destination.entity.UserDestination;
import com.plac.domain.destination.repository.DestinationRepository;
import com.plac.domain.destination.repository.UserDestinationRepository;
import com.plac.domain.place.dto.response.PlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.repository.place.PlaceRepository;
import com.plac.domain.plan.dto.PlansInformation;
import com.plac.domain.plan.dto.request.CommunityPlanRequest;
import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanFixRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.response.GetMyListPlansResponseDto;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.entity.*;
import com.plac.domain.plan.repository.bookmark.BookmarkPlanRepository;
import com.plac.domain.plan.repository.favoritePlan.FavoritePlanRepository;
import com.plac.domain.plan.repository.plan.PlanQueryRepositoryImpl;
import com.plac.domain.plan.repository.plan.PlanRepository;
import com.plac.domain.plan.repository.planDibs.PlanDibsRepository;
import com.plac.domain.plan.repository.planPlaceMapping.PlanPlaceMappingRepository;
import com.plac.domain.plan.repository.planTag.PlanTagRepository;
import com.plac.domain.plan.repository.planTagMapping.PlanTagMappingRepository;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.common.BadRequestException;
import com.plac.exception.common.DataNotFoundException;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlanService {

    private final PlaceRepository placeRepository;
    private final PlanRepository planRepository;
    private final PlanQueryRepositoryImpl planQueryRepository;
    private final UserRepository userRepository;
    private final PlanPlaceMappingRepository planPlaceMappingRepository;
    private final PlanTagRepository planTagRepository;
    private final PlanTagMappingRepository planTagMappingRepository;
    private final FavoritePlanRepository favoritePlanRepository;
    private final BookmarkPlanRepository bookmarkPlanRepository;
    private final DestinationRepository destinationRepository;
    private final UserDestinationRepository userDestinationRepository;
    private final PlanDibsRepository planDibsRepository;

    @Transactional
    public PlanCreateResponse createPlan(PlanCreateRequest planRequest) {
        User user = userRepository.findById(SecurityContextHolderUtil.getUserId()).get();

        List<PlaceInfo> placeInfoList = planRequest.getPlaceList();
        // PlaceInfo seq 오름차순으로 원소를 정렬
        placeInfoList.sort(Comparator.comparingInt(PlaceInfo::getSeq));

        List<Place> placeList = placeInfoList.stream()
                .map(placeInfo -> {
                    Long kakaoPlaceId = placeInfo.getKakaoPlaceId();
                    return placeRepository.findByKakaoPlaceId(kakaoPlaceId)
                            .orElseGet(() -> createAndSavePlace(placeInfo));
                })
                .collect(Collectors.toList());

        Destination destination = createNewDestination(planRequest);
        createUserDestination(user, destination);

        Plan savedPlan = createNewPlan(planRequest, user, placeList);
        return new PlanCreateResponse(savedPlan.getId());
    }

    private void createUserDestination(User user, Destination destination) {
        UserDestination userDestination = new UserDestination(user, destination);
        userDestinationRepository.save(userDestination);
    }

    private Destination createNewDestination(PlanCreateRequest planRequest) {
        String destinationName = planRequest.getDestinationName();
        Destination destination = destinationRepository.findByName(destinationName)
                .orElseGet(() -> destinationRepository.save(
                        Destination.builder()
                                .name(destinationName)
                                .build()
                ));

        return destination;
    }

    private Place createAndSavePlace(PlaceInfo placeInfo) {
        Place newPlace = Place.builder()
                .placeName(placeInfo.getPlaceName())
                .type(placeInfo.getType())
                .kakaoPlaceId(placeInfo.getKakaoPlaceId())
                .x(placeInfo.getX())
                .y(placeInfo.getY())
                .thumbnailImageUrl(placeInfo.getImageUrl())
                .streetNameAddress(placeInfo.getAddress())
                .build();

        return placeRepository.save(newPlace);
    }

    public Plan createNewPlan(PlanCreateRequest planRequest, User user, List<Place> placeList) {
        Plan plan = Plan.builder()
                .destinationName(planRequest.getDestinationName())
                .name(planRequest.getPlanName())
                .user(user)
                .build();

        Plan savedPlan = planRepository.save(plan);

        planPlaceMappingRepository.saveAll(
                IntStream.range(0, placeList.size())
                .mapToObj(sequence -> {
                    Place place = placeList.get(sequence);
                    return PlanPlaceMapping.builder()
                            .plan(savedPlan)
                            .place(place)
                            .seq(sequence)
                            .build();
                })
                .collect(Collectors.toList())
        );
        return savedPlan;
    }

    @Transactional
    public void sharePlanToCommunity(PlanShareRequest planRequest, Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new DataNotFoundException());

        plan.changeOpenness(true);
        createPlanTagMappings(planRequest, plan);

        List<String> etcTags = planRequest.getEtc();
        createEtcPlanTagMappings(plan, etcTags);
    }

    private void createEtcPlanTagMappings(Plan plan, List<String> etcTags) {
        for (String etcTag : etcTags) {
            PlanTag planTag = planTagRepository.findByTagName(etcTag)
                    .orElseGet(() ->
                            planTagRepository.save(PlanTag.builder().tagName(etcTag).build())
                    );

            PlanTagMapping planTagMapping = new PlanTagMapping(plan, planTag);

            planTagMappingRepository.save(planTagMapping);
        }
    }

    private void createPlanTagMappings(PlanShareRequest planRequest, Plan plan) {
        List<Long> tagIdlist = planRequest.getTagIds();

        for (Long tagId : tagIdlist) {
            PlanTag planTag = planTagRepository.findById(tagId).get();
            PlanTagMapping planTagMapping = new PlanTagMapping(plan, planTag);
            planTagMappingRepository.save(planTagMapping);
        }
    }

    public void fixPlan(PlanFixRequest planRequest, Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow (() -> new DataNotFoundException());

        List<PlanPlaceMapping> planPlaceMappings = planPlaceMappingRepository.findByPlanId(planId);

        for (PlanPlaceMapping planPlaceMapping : planPlaceMappings) {
            planPlaceMappingRepository.delete(planPlaceMapping);
        }
    }

    @Transactional
    public void deletePlan(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new DataNotFoundException());
        planRepository.delete(plan);
    }

    public void makeFavoritePlan(Long planId) {
        Long userId = SecurityContextHolderUtil.getUserId();
        User user = userRepository.findById(userId).get();

        Plan plan = planRepository.findById(planId).orElseThrow (() -> new DataNotFoundException());

        Optional<FavoritePlan> favoritePlanOpt = favoritePlanRepository.findByUserIdAndPlanId(userId, planId);

        if (favoritePlanOpt.isPresent()) {
            throw new BadRequestException("이미 좋아요를 눌렀습니다.");
        }

        FavoritePlan favoritePlan = FavoritePlan.builder()
                .plan(plan)
                .user(user)
                .favorite(true)
                .build();

        favoritePlanRepository.save(favoritePlan);
    }

    /**
     * 추후 캐시로 성능 개선해보기!!*/
    @Transactional
    public void clearFavoritePlan(Long planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        FavoritePlan favoritePlan = favoritePlanRepository.findByUserIdAndPlanId(userId, planId)
                .orElseThrow(() -> new BadRequestException("no favorite plan")
        );

        favoritePlanRepository.delete(favoritePlan);
    }

    public void saveBookMarkPlan(Long planId) {
        User user = userRepository.findById(SecurityContextHolderUtil.getUserId()).get();
        Plan plan = planRepository.findById(planId).orElseThrow (() -> new DataNotFoundException());

        BookmarkPlan bookmarkPlan = new BookmarkPlan(plan, user);
        bookmarkPlanRepository.save(bookmarkPlan);
    }

    public void deleteBookMarkPlan(Long planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        BookmarkPlan bookmarkPlan = bookmarkPlanRepository.findByUserIdAndPlanId(userId, planId)
                .orElseThrow(()-> new DataNotFoundException());

        bookmarkPlanRepository.delete(bookmarkPlan);
    }

    // TODO: 리팩토링, 쿼리 개선
    public List<PlansInformation> getCommunityPlans(CommunityPlanRequest planRequest) {
        List<Long> planIdList = null;
        List<PlansInformation> result = new ArrayList<>();

        String sortBy = planRequest.getSortBy();
        planIdList = planRepository.findPlanIdsByDestinationName(planRequest.getDestination());

        if (planIdList.isEmpty()) return new ArrayList<>();

        if (!planRequest.getPlace().isEmpty()) {
            planIdList = planPlaceMappingRepository.findPlanIdsByPlaceName(planIdList, planRequest.getPlace());
        }
        if (planRequest.getAgeGroup() != null && !planIdList.isEmpty()) {
            System.out.println("planRequest.getAgeGroup() = " + planRequest.getAgeGroup());
            planIdList = planRepository.findPlanIdsByUserAgeGroup(planIdList, planRequest.getAgeGroup());
        }
        if (!planRequest.getGender().isEmpty() && !planIdList.isEmpty()) {
            planIdList = planRepository.findPlanIdsByUserGender(planIdList, planRequest.getGender());
        }
        if (!planRequest.getTags().isEmpty() && !planIdList.isEmpty()) {
            String[] split = planRequest.getTags().split(",");
            planIdList = planTagRepository.findPlanIdsWithAllTags(planIdList, Arrays.asList(split), split.length);
        }
        if (!planIdList.isEmpty()) {
            switch (sortBy) {
                case "최신순":
                    planIdList = planQueryRepository.findPlanIdsByCreatedAtDesc(planIdList);
                    break;
                case "저장순":
                    planIdList = planQueryRepository.findPlanIdsByPlanDibsDesc(planIdList);
                    break;
                case "인기순":
                    planIdList = planQueryRepository.findPlanIdsByFavoritePlanDesc(planIdList);
                    break;
            }
        }
        for (Long planId : planIdList) {
            System.out.println(planId);
        }
        if (planIdList.isEmpty()) return new ArrayList<>();

        for (long planId : planIdList) {
            createPlanInformation(result, planId);
        }
        return result;
    }

    private void createPlanInformation(List<PlansInformation> result, long planId) {
        Plan plan = planRepository.findById(planId).get();
        User user = plan.getUser();

        List<FavoritePlan> favoritePlans = favoritePlanRepository.findByPlanId(plan.getId());
        List<PlanDibs> bookmarkPlans = planDibsRepository.findDibsByPlanId(plan.getId());

        boolean isFavorite = favoritePlanRepository.findByUserIdAndPlanId(user.getId(), plan.getId()).isPresent();
        boolean isBookmarked = planDibsRepository.findDibsByUserIdAndPlanId(user.getId(), plan.getId()).isPresent();

        List<Place> placeList = planPlaceMappingRepository.findPlacesByPlanId(plan.getId());
        List<PlaceInfo> placeInfoList = new ArrayList<>();

        List<String> tagList = new ArrayList<>();
        List<PlanTagMapping> planTagMappings = planTagMappingRepository.findTagsByPlanId(plan.getId());

        for (PlanTagMapping planTag : planTagMappings) {
            String tagName = planTag.getPlanTag().getTagName();
            tagList.add(tagName);
        }

        for (Place place : placeList) {
            PlaceInfo placeInfo = PlaceInfo.of(place);
            placeInfoList.add(placeInfo);
        }

        PlansInformation plansInfo = PlansInformation.builder()
                .planId(plan.getId())
                .userProfileName(user.getProfileName())
                .profileImageUrl(user.getProfileImageUrl())
                .minuteDifferences(Duration.between(plan.getUpdatedAt(), LocalDateTime.now()).toMinutes())
                .planName(plan.getName())
                .favoriteCount(favoritePlans.size())
                .bookmarkCount(bookmarkPlans.size())
                .isFavorite(isFavorite)
                .isBookmarked(isBookmarked)
                .placeInfoList(placeInfoList)
                .tagList(tagList)
                .build();

        result.add(plansInfo);
    }

    public List<PlansInformation> getMostPopularPlans() {
        PageRequest pageable = PageRequest.of(0, 5); // 최대 5개 결과를 가져오기 위한 Pageable 생성

        List<Object[]> resultList = favoritePlanRepository.findPlansOrderByTotalLikesDesc(pageable);

        List<Long> planIds = new ArrayList<>();

        for (Object[] result : resultList) {
            Long planId = (Long) result[0];
            planIds.add(planId);
        }

        List<PlansInformation> result = new ArrayList<>();

        for (Long planId : planIds) {
            Plan plan = planRepository.findById(planId).get();
            String planName = plan.getName();

            User user = plan.getUser();

            List<PlanPlaceMapping> planPlaceMappings = plan.getPlanPlaceMappings();
            List<PlaceInfo> placeInfoList = new ArrayList<>();

            for (PlanPlaceMapping planPlaceMapping : planPlaceMappings) {
                Place place = planPlaceMapping.getPlace();
                PlaceInfo buildPlaceInfo = PlaceInfo.of(place);
                placeInfoList.add(buildPlaceInfo);
            }
            // PlaceInfo 객체를 seq 오름차순으로 정렬
            Collections.sort(placeInfoList, Comparator.comparingInt(PlaceInfo::getSeq));

            List<FavoritePlan> favoritePlans = favoritePlanRepository.findByPlanId(plan.getId());
            int favoriteCount = favoritePlans.size();

            List<PlanDibs> planDibs = planDibsRepository.findDibsByPlanId(plan.getId());
            int bookmarkCount = planDibs.size();

            List<PlanTagMapping> planTagMappingList = planTagMappingRepository.findTagsByPlanId(plan.getId());

            List<String> tagList = new ArrayList<>();

            for (PlanTagMapping planTagMapping : planTagMappingList) {
                PlanTag planTag = planTagMapping.getPlanTag();

                String tagName = planTag.getTagName();
                tagList.add(tagName);
            }

            PlansInformation temp = PlansInformation.builder()
                    .planId(planId)
                    .userProfileName(user.getProfileName())
                    .profileImageUrl(user.getProfileImageUrl())
                    .planName(planName)
                    .minuteDifferences(Duration.between(plan.getUpdatedAt(), LocalDateTime.now()).toMinutes())
                    .placeInfoList(placeInfoList)
                    .favoriteCount(favoriteCount)
                    .bookmarkCount(bookmarkCount)
                    .build();

            result.add(temp);
        }

        return result;

    }

    @Transactional
    public void triggerDibsMyListPlan(Long planId) {
        final User user = userRepository.findById(SecurityContextHolderUtil.getUserId())
                .orElseThrow(() -> new RuntimeException("로그인 사용자 없음 예외추가"));
        final long userId = user.getId();

        Optional<PlanDibs> getDibsPlan = planDibsRepository.findDibsByUserIdAndPlanId(userId, planId);
        if (getDibsPlan.isEmpty()) {
            addDibsPlan(userId, planId);
        } else {
            PlanDibs planDibs = getDibsPlan.orElseThrow(() -> new RuntimeException("찜되어 있지 않음 예외 추가"));
            deleteDibsPlan(planDibs);
        }
    }

    private void addDibsPlan(Long userId, Long planId) {
        PlanDibs created = PlanDibs.create(planId, userId);
        planDibsRepository.save(created);
    }

    private void deleteDibsPlan(PlanDibs planDibs) {
        planDibsRepository.delete(planDibs);
    }

    public List<GetMyListPlansResponseDto> getMyListPlans() {
        final User user = userRepository.findById(SecurityContextHolderUtil.getUserId())
                .orElseThrow(() -> new RuntimeException("로그인 사용자 없음 예외추가"));
        final long userId = user.getId();

        return planRepository.findPlanDibsByUserId(userId).stream()
                .map(plan -> {
                    List<String> placeUrls = planPlaceMappingRepository.findPlacesByPlanId(plan.getId()).stream()
                            .map(Place::getThumbnailImageUrl)
                            .collect(Collectors.toList());
                    return new GetMyListPlansResponseDto(user.getProfileName(), plan, placeUrls);
                })
                .collect(Collectors.toList());
    }

    public void deleteMySharedPlan(Long planId) {

    }
}
