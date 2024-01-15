package com.plac.domain.plan.service;

import com.plac.domain.destination.entity.Destination;
import com.plac.domain.destination.repository.DestinationRepository;
import com.plac.domain.place.dto.response.PlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.repository.PlaceRepository;
import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanFixRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.dto.response.PlansInformation;
import com.plac.domain.plan.entity.*;
import com.plac.domain.plan.repository.*;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.plan.BookmarkPlanNotFoundException;
import com.plac.exception.plan.FavoritePlanException;
import com.plac.exception.plan.PlanNotFoundException;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlaceRepository placeRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanPlaceMappingRepository planPlaceMappingRepository;
    private final PlanTagRepository planTagRepository;
    private final PlanTagMappingRepository planTagMappingRepository;
    private final FavoritePlanRepository favoritePlanRepository;
    private final BookmarkPlanRepository bookmarkPlanRepository;
    private final DestinationRepository destinationRepository;

    @Transactional
    public PlanCreateResponse createPlan(PlanCreateRequest planRequest) {
        User user = userRepository.findById(SecurityContextHolderUtil.getUserId()).get();

        List<PlaceInfo> placeInfoList = planRequest.getPlaceList();
        // PlaceInfo seq 오름차순으로 원소를 정렬
        Collections.sort(placeInfoList, Comparator.comparingInt(PlaceInfo::getSeq));

        List<Place> placeList = new ArrayList<>();

        for (PlaceInfo placeInfo : placeInfoList) {
            Long kakaoPlaceId = placeInfo.getKakaoPlaceId();

            placeRepository.findByKakaoPlaceId(kakaoPlaceId).ifPresentOrElse(
                    placeList::add,
                    () -> placeList.add(createAndSavePlace(placeInfo))
            );
        }
        createNewDestination(planRequest);
        Plan savedPlan = createNewPlan(planRequest, user, placeList);

        return new PlanCreateResponse(savedPlan.getId());
    }

    private void createNewDestination(PlanCreateRequest planRequest) {
        String destinationName = planRequest.getDestinationName();

        if(!destinationRepository.findByName(destinationName).isPresent()) {
            destinationRepository.save(
                    Destination.builder()
                            .name(destinationName)
                            .build()
            );
        }
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
                .open(false)
                .build();

        Plan savedPlan = planRepository.save(plan);

        int sequence = 0;

        for (Place place : placeList) {
            PlanPlaceMapping planPlaceMapping = PlanPlaceMapping.builder()
                    .plan(savedPlan)
                    .place(place)
                    .seq(sequence++)
                    .build();
            planPlaceMappingRepository.save(planPlaceMapping);
        }

        return savedPlan;
    }

    @Transactional
    public void sharePlanToCommunity(PlanShareRequest planRequest, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("존재하지 않는 planId 입니다.")
        );
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

            PlanTagMapping planTagMapping = PlanTagMapping.builder()
                    .plan(plan)
                    .planTagId(planTag.getId())
                    .build();

            planTagMappingRepository.save(planTagMapping);
        }
    }

    private void createPlanTagMappings(PlanShareRequest planRequest, Plan plan) {
        List<Long> tagIdlist = planRequest.getTagIds();

        for (Long tagId : tagIdlist) {
            PlanTagMapping planTagMapping = PlanTagMapping.builder()
                    .plan(plan)
                    .planTagId(tagId)
                    .build();
            planTagMappingRepository.save(planTagMapping);
        }
    }

    public void fixPlan(PlanFixRequest planRequest, Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow (
                () -> new PlanNotFoundException("존재하지 않는 planId 입니다.")
        );

        List<PlanPlaceMapping> planPlaceMappings = planPlaceMappingRepository.findByPlanId(planId);

        for (PlanPlaceMapping planPlaceMapping : planPlaceMappings) {
            planPlaceMappingRepository.delete(planPlaceMapping);
        }
    }

    @Transactional
    public void deletePlan(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new PlanNotFoundException("plan not found")
        );
        List<PlanPlaceMapping> planPlaceMapping = planPlaceMappingRepository.findByPlanId(planId);

        planRepository.delete(plan);
    }

    /**
     * 추후 캐시로 성능 개선해보기!!*/
    public void makeFavoritePlan(Long planId) {
        Long userId = SecurityContextHolderUtil.getUserId();
        User user = userRepository.findById(userId).get();

        Plan plan = planRepository.findById(planId).orElseThrow (
                () -> new PlanNotFoundException("plan not found")
        );

        Optional<FavoritePlan> favoritePlanOpt = favoritePlanRepository.findByUserIdAndPlanId(userId, planId);

        if (favoritePlanOpt.isPresent()) {
            throw new FavoritePlanException("이미 좋아요를 눌렀습니다.");
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
                .orElseThrow(() -> new FavoritePlanException("no favorite plan")
        );

        favoritePlanRepository.delete(favoritePlan);
    }

    public void saveBookMarkPlan(Long planId) {
        User user = userRepository.findById(SecurityContextHolderUtil.getUserId()).get();

        Plan plan = planRepository.findById(planId).orElseThrow (
                () -> new PlanNotFoundException("plan not found")
        );

        BookmarkPlan bookmarkPlan = BookmarkPlan.builder()
                .user(user)
                .plan(plan)
                .build();

        bookmarkPlanRepository.save(bookmarkPlan);
    }

    public void deleteBookMarkPlan(Long planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        BookmarkPlan bookmarkPlan = bookmarkPlanRepository.findByUserIdAndPlanId(userId, planId)
                .orElseThrow(
                        ()-> new BookmarkPlanNotFoundException("bookmark-plan not found")
                );
        bookmarkPlanRepository.delete(bookmarkPlan);
    }

    public List<PlansInformation> getPlansByDestinations(String destinationName) {

        Long userId = SecurityContextHolderUtil.getUserId();
        User user = userRepository.findById(userId).get();

        String userProfileName = user.getProfileName();

        List<Plan> plans = planRepository.findByDestinationName(destinationName);

        List<PlansInformation> result = new ArrayList<>();

        for (Plan plan : plans) {
            // 두 LocalDateTime 객체 간의 시간 차이 계산
            Duration duration = Duration.between(plan.getUpdatedAt(), LocalDateTime.now());
            long minutesDifference = duration.toMinutes();

            String planName = plan.getName();

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

            List<BookmarkPlan> bookmarkPlans = bookmarkPlanRepository.findByPlanId(plan.getId());
            int bookmarkCount = bookmarkPlans.size();

            List<PlanTagMapping> planTagMappingList = planTagMappingRepository.findByPlanId(plan.getId());

            List<String> tagList = new ArrayList<>();

            for (PlanTagMapping planTagMapping : planTagMappingList) {
                Long planTagId = planTagMapping.getPlanTagId();
                PlanTag planTag = planTagRepository.findById(planTagId).get();

                String tagName = planTag.getTagName();
                tagList.add(tagName);
            }

            PlansInformation temp = PlansInformation.builder()
                    .userProfileName(userProfileName)
                    .minuteDifferences(minutesDifference)
                    .planName(planName)
                    .placeInfoList(placeInfoList)
                    .favoriteCount(favoriteCount)
                    .bookmarkCount(bookmarkCount)
                    .tagList(tagList)
                    .build();

            result.add(temp);
        }

        Collections.sort(result, Comparator.comparingInt(PlansInformation::getFavoriteCount).reversed());

        return result;
    }

    public List<PlansInformation> getMostPopularPlans() {
        PageRequest pageable = PageRequest.of(0, 5); // 최대 5개 결과를 가져오기 위한 Pageable 생성

        List<Object[]> resultList = favoritePlanRepository.findPlansOrderByTotalLikesDesc(pageable);

        List<Long> planIds = new ArrayList<>();

        for (Object[] result : resultList) {
            Long planId = (Long) result[0];
            planIds.add(planId);
        }

        for (Long planId : planIds) {
            System.out.println("planId = " + planId);
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

            List<BookmarkPlan> bookmarkPlans = bookmarkPlanRepository.findByPlanId(plan.getId());
            int bookmarkCount = bookmarkPlans.size();

            List<PlanTagMapping> planTagMappingList = planTagMappingRepository.findByPlanId(plan.getId());

            List<String> tagList = new ArrayList<>();

            for (PlanTagMapping planTagMapping : planTagMappingList) {
                Long planTagId = planTagMapping.getPlanTagId();
                PlanTag planTag = planTagRepository.findById(planTagId).get();

                String tagName = planTag.getTagName();
                tagList.add(tagName);
            }

            PlansInformation temp = PlansInformation.builder()
                    .userProfileName(user.getProfileName())
                    .planName(planName)
                    .placeInfoList(placeInfoList)
                    .favoriteCount(favoriteCount)
                    .bookmarkCount(bookmarkCount)
                    .tagList(tagList)
                    .build();

            result.add(temp);
        }

        return result;

    }
}
