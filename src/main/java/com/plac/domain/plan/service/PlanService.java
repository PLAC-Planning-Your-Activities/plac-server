package com.plac.domain.plan.service;

import com.plac.domain.destination.entity.Destination;
import com.plac.domain.destination.entity.DestinationMapping;
import com.plac.domain.destination.repository.DestinationMappingRepository;
import com.plac.domain.destination.repository.DestinationRepository;
import com.plac.domain.place.dto.response.PlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.repository.PlaceRepository;
import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanFixRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.request.TagListRequest;
import com.plac.domain.plan.dto.response.MyListPlan;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.dto.response.PlansInformation;
import com.plac.domain.plan.entity.*;
import com.plac.domain.plan.repository.*;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.common.DataNotFoundException;
import com.plac.exception.plan.FavoritePlanException;
import com.plac.exception.plan.PlanNotFoundException;
import com.plac.exception.plan.PlanSharedToCummunityException;
import com.plac.exception.user.UserNotFoundException;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final DestinationMappingRepository destinationMappingRepository;

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
        createNewDestination(planRequest, user);
        Plan savedPlan = createNewPlan(planRequest, user, placeList);

        return new PlanCreateResponse(savedPlan.getId());
    }

    private void createNewDestination(PlanCreateRequest planRequest, User user) {
        String destinationName = planRequest.getDestinationName();
        Optional<Destination> destinationOpt = destinationRepository.findByName(destinationName);
        Destination destination;

        if (!destinationOpt.isPresent()) {
            destination = destinationRepository.save(Destination.builder()
                    .name(planRequest.getDestinationName())
                    .build());
        } else destination = destinationOpt.get();

        destinationMappingRepository.save(
                DestinationMapping.builder()
                        .user(user)
                        .destination(destination)
                        .build()
        );
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
        if(plan.getUser().getId() != SecurityContextHolderUtil.getUserId()){
            throw new DataNotFoundException();
        }
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
    public void deletePlan(Long planId, boolean isMyPlan) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new DataNotFoundException());

        if (plan.isDeleted() == true) {
            throw new DataNotFoundException();
        }
        if (plan.isOpen() == true){
            throw new PlanSharedToCummunityException();
        }

        if (isMyPlan == false) {  // 내가 만든 플랜이 아닌 경우 => 삭제 불가 (북마크만 해제)
            bookmarkPlanRepository.deleteByUserIdAndPlanId(SecurityContextHolderUtil.getUserId(), planId);
        }
        else if (isMyPlan == true) {  // 내가 만든 플랜인 경우
            plan.setDeleted(true);
        }
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
        User user = userRepository.findById(SecurityContextHolderUtil.getUserId()).orElseThrow(
                () -> new UserNotFoundException("user not found")
        );
        Plan plan = planRepository.findById(planId).orElseThrow (
                () -> new PlanNotFoundException("plan not found")
        );
        if (user.getId() == plan.getUser().getId()){
            throw new DataNotFoundException();
        }

        BookmarkPlan bookmarkPlan = BookmarkPlan.builder()
                .user(user)
                .plan(plan)
                .build();
        bookmarkPlanRepository.save(bookmarkPlan);
    }

    public void deleteBookMarkPlan(Long planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        BookmarkPlan bookmarkPlan = bookmarkPlanRepository.findByUserIdAndPlanId(userId, planId)
                .orElseThrow(()-> new DataNotFoundException());
        bookmarkPlanRepository.delete(bookmarkPlan);
    }

    public List<PlansInformation> getPlansByDestinations(String destinationName, String sortBy, TagListRequest tagListRequest, String placeName) {

        Long userId = SecurityContextHolderUtil.getUserId();
        User user = userRepository.findById(userId).get();

        String userProfileName = user.getProfileName();

        List<Plan> plans = planRepository.findByDestinationName(destinationName);
        Set<Long> planIds = plans.stream().map(Plan::getId).collect(Collectors.toSet());

        Map<Long, Integer> favoriteCountMap = getFavoriteCountByPlanIds(planIds);
        Map<Long, Integer> bookmarkCountMap = getBookMarkCountByPlanIds(planIds);
        Map<Long, List<String>> tagListMap = getTagListMapByPlanIds(planIds);

        List<PlansInformation> result = new ArrayList<>();

        for (Plan plan : plans) {
            Duration duration = Duration.between(plan.getUpdatedAt(), LocalDateTime.now());
            long minutesDifference = duration.toMinutes();
            String planName = plan.getName();

            // 기존의 계산 로직 유지
            List<PlaceInfo> placeInfoList = plan.getPlanPlaceMappings().stream()
                    .map(PlanPlaceMapping::getPlace)
                    .map(PlaceInfo::of)
                    .sorted(Comparator.comparingInt(PlaceInfo::getSeq))
                    .collect(Collectors.toList());

            int favoriteCount = favoriteCountMap.getOrDefault(plan.getId(), 0);
            int bookmarkCount = bookmarkCountMap.getOrDefault(plan.getId(), 0);
            List<String> tagList = tagListMap.getOrDefault(plan.getId(), Collections.emptyList());

            PlansInformation temp = PlansInformation.builder()
                    .userId(plan.getUser().getId())
                    .userProfileUrl(plan.getUser().getProfileImageUrl())
                    .userProfileName(userProfileName)
                    .minuteDifferences(minutesDifference)
                    .planId(plan.getId())
                    .planName(planName)
                    .placeInfoList(placeInfoList)
                    .favoriteCount(favoriteCount)
                    .bookmarkCount(bookmarkCount)
                    .tagList(tagList)
                    .build();

            result.add(temp);
        }

        List<String> userTags = tagListRequest.getTags();
        // 필터링 로직: 사용자가 지정한 태그들을 모두 포함하는 PlansInformation 객체만을 선택
        if (userTags != null && !userTags.isEmpty()) {
            result = result.stream()
                    .filter(plansInfo -> userTags.stream().allMatch(tag -> plansInfo.getTagList().contains(tag)))
                    .collect(Collectors.toList());
        }

        if (sortBy.equals("최신순")) {
            Collections.sort(result, Comparator.comparingLong(PlansInformation::getMinuteDifferences));
        } else if (sortBy.equals("인기순")) {
            Collections.sort(result, Comparator.comparingInt(PlansInformation::getFavoriteCount).reversed());
        } else if (sortBy.equals("저장순")) {
            Collections.sort(result, Comparator.comparingInt(PlansInformation::getBookmarkCount).reversed());
        }

        if (!placeName.equals("NONE"))
            result = filterPlansByPlaceName(placeName, result);

        return result;
    }

    public List<PlansInformation> getMostPopularPlans() {
        PageRequest pageable = PageRequest.of(0, 5); // 최대 5개 결과를 가져오기 위한 Pageable 생성
        List<Object[]> resultList = favoritePlanRepository.findPlansOrderByTotalLikesDesc(pageable);

        List<Long> planIds = resultList.stream()
                .map(result -> (Long) result[0])
                .collect(Collectors.toList());

        Set<Long> planIdSet = new HashSet<>(planIds);

        // Bulk 연산으로 모든 Plan 객체를 가져옴
        List<Plan> plans = planRepository.findByIdIn(planIds);

        // 관련 데이터를 Bulk 연산으로 가져옴
        List<PlanPlaceMapping> planPlaceMappings = planPlaceMappingRepository.findByPlanIdIn(planIds);
        Map<Long, List<PlanPlaceMapping>> planPlaceMappingsMap = planPlaceMappings.stream()
                .collect(Collectors.groupingBy(mapping -> mapping.getPlan().getId()));

        Map<Long, Integer> favoriteCountMap = getFavoriteCountByPlanIds(planIdSet);
        Map<Long, Integer> bookmarkCountMap = getBookMarkCountByPlanIds(planIdSet);
        Map<Long, List<String>> tagListMap = getTagListMapByPlanIds(planIdSet);

        List<PlansInformation> result = plans.stream().map(plan -> {
            // planPlaceMappingsMap 에서 Plan ID에 해당하는 PlanPlaceMapping 객체들의 리스트를 가져옴
            List<PlanPlaceMapping> mappings = planPlaceMappingsMap.getOrDefault(plan.getId(), Collections.emptyList());

            // PlanPlaceMapping 객체들을 PlaceInfo 객체들로 변환
            List<PlaceInfo> placeInfoList = mappings.stream()
                    .map(PlanPlaceMapping::getPlace) // PlanPlaceMapping에서 Place 객체를 가져옴
                    .map(PlaceInfo::of) // Place 객체를 PlaceInfo 객체로 변환
                    .collect(Collectors.toList());

            int favoriteCount = favoriteCountMap.getOrDefault(plan.getId(), 0);
            int bookmarkCount = bookmarkCountMap.getOrDefault(plan.getId(), 0);
            List<String> tagList = tagListMap.getOrDefault(plan.getId(), Collections.emptyList());

            return PlansInformation.builder()
                    .userId(plan.getUser().getId())
                    .userProfileUrl(plan.getUser().getProfileImageUrl())
                    .userProfileName(plan.getUser().getProfileName())
                    .planId(plan.getId())
                    .planName(plan.getName())
                    .placeInfoList(placeInfoList)
                    .favoriteCount(favoriteCount)
                    .bookmarkCount(bookmarkCount)
                    .tagList(tagList)
                    .build();
        }).collect(Collectors.toList());

        return result;

    }

    public Map<Long, Integer> getFavoriteCountByPlanIds(Set<Long> planIds) {
        List<Object[]> counts = favoritePlanRepository.countByPlanIds(planIds);
        return counts.stream().collect(Collectors.toMap(
                count -> (Long) count[0], // Plan ID
                count -> ((Long) count[1]).intValue() // Count
        ));
    }

    public Map<Long, Integer> getBookMarkCountByPlanIds(Set<Long> planIds) {
        List<Object[]> counts = bookmarkPlanRepository.countByPlanIds(planIds);
        return counts.stream().collect(Collectors.toMap(
                count -> (Long) count[0], // Plan ID
                count -> ((Long) count[1]).intValue() // Count
        ));
    }

    public Map<Long, List<String>> getTagListMapByPlanIds(Set<Long> planIds) {
        List<Object[]> planTagIdsData = planTagMappingRepository.findPlanTagIdsByPlanIds(planIds);
        Set<Long> planTagIds = planTagIdsData.stream()
                .map(data -> (Long) data[1])
                .collect(Collectors.toSet());

        List<Object[]> tagsData = planTagRepository.findTagNamesByPlanTagIds(planTagIds);
        Map<Long, String> tagNameMap = tagsData.stream()
                .collect(Collectors.toMap(
                        data -> (Long) data[0],
                        data -> (String) data[1]
                ));

        Map<Long, List<String>> tagListMap = new HashMap<>();
        for (Object[] data : planTagIdsData) {
            Long planId = (Long) data[0];
            Long planTagId = (Long) data[1];
            String tagName = tagNameMap.get(planTagId);

            tagListMap.computeIfAbsent(planId, k -> new ArrayList<>()).add(tagName);
        }

        return tagListMap;
    }

    public List<PlansInformation> filterPlansByPlaceName(String placeNameKeyword, List<PlansInformation> plansList) {
        String searchTerm = placeNameKeyword.toLowerCase();

        return plansList.stream()
                .filter(plansInfo -> plansInfo.getPlaceInfoList().stream()
                        .anyMatch(placeInfo -> {
                            String placeNameLower = placeInfo.getPlaceName().toLowerCase();
                            return Arrays.asList(placeNameLower.split(" ")).contains(searchTerm)
                                    || placeNameLower.equals(searchTerm); // 추가된 조건
                        }))
                .collect(Collectors.toList());
    }


    public List<MyListPlan> getMyPlans() {
        List<Plan> planList = planRepository.findByUserId(SecurityContextHolderUtil.getUserId());
        List<MyListPlan> result = new ArrayList<>();

        for (Plan plan : planList) {
            MyListPlan myListPlan = MyListPlan.builder()
                    .planId(plan.getId())
                    .planName(plan.getName())
                    .imageUrl(plan.getImageUrl())
                    .destinationName(plan.getDestinationName())
                    .myPlan(true)
                    .build();

            result.add(myListPlan);
        }

        return result;
    }

    public PlansInformation getPlanDetails(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new DataNotFoundException()
        );

        User planUser = plan.getUser();

        Set<Long> planIds = new HashSet<>();
        planIds.add(plan.getId());

        Map<Long, Integer> favoriteCountMap = getFavoriteCountByPlanIds(planIds);
        Map<Long, Integer> bookmarkCountMap = getBookMarkCountByPlanIds(planIds);
        Map<Long, List<String>> tagListMap = getTagListMapByPlanIds(planIds);

        Duration duration = Duration.between(plan.getUpdatedAt(), LocalDateTime.now());
        long minutesDifference = duration.toMinutes();

        String planName = plan.getName();

        // 기존의 계산 로직 유지
        List<PlaceInfo> placeInfoList = plan.getPlanPlaceMappings().stream()
                .map(PlanPlaceMapping::getPlace)
                .map(PlaceInfo::of)
                .sorted(Comparator.comparingInt(PlaceInfo::getSeq))
                .collect(Collectors.toList());

        int favoriteCount = favoriteCountMap.getOrDefault(plan.getId(), 0);
        int bookmarkCount = bookmarkCountMap.getOrDefault(plan.getId(), 0);
        List<String> tagList = tagListMap.getOrDefault(plan.getId(), Collections.emptyList());

        PlansInformation result = PlansInformation.builder()
                .userId(planUser.getId())
                .userProfileUrl(planUser.getProfileImageUrl())
                .userProfileName(planUser.getProfileName())
                .minuteDifferences(minutesDifference)
                .planId(plan.getId())
                .planName(planName)
                .placeInfoList(placeInfoList)
                .favoriteCount(favoriteCount)
                .bookmarkCount(bookmarkCount)
                .tagList(tagList)
                .build();

        return result;

    }

    public List<PlansInformation> getSharedPlans() {
        List<Plan> sharedPlans = planRepository.findByUserIdAndOpen(SecurityContextHolderUtil.getUserId());

        List<Long> planIds = new ArrayList<>();

        for (Plan plan : sharedPlans) {
            planIds.add(plan.getId());
        }

        Set<Long> planIdSet = new HashSet<>(planIds);

        // Bulk 연산으로 모든 Plan 객체를 가져옴
        List<Plan> plans = planRepository.findByIdIn(planIds);

        // 관련 데이터를 Bulk 연산으로 가져옴
        List<PlanPlaceMapping> planPlaceMappings = planPlaceMappingRepository.findByPlanIdIn(planIds);
        Map<Long, List<PlanPlaceMapping>> planPlaceMappingsMap = planPlaceMappings.stream()
                .collect(Collectors.groupingBy(mapping -> mapping.getPlan().getId()));

        Map<Long, Integer> favoriteCountMap = getFavoriteCountByPlanIds(planIdSet);
        Map<Long, Integer> bookmarkCountMap = getBookMarkCountByPlanIds(planIdSet);
        Map<Long, List<String>> tagListMap = getTagListMapByPlanIds(planIdSet);

        List<PlansInformation> result = plans.stream().map(plan -> {
            // planPlaceMappingsMap 에서 Plan ID에 해당하는 PlanPlaceMapping 객체들의 리스트를 가져옴
            List<PlanPlaceMapping> mappings = planPlaceMappingsMap.getOrDefault(plan.getId(), Collections.emptyList());

            // PlanPlaceMapping 객체들을 PlaceInfo 객체들로 변환
            List<PlaceInfo> placeInfoList = mappings.stream()
                    .map(PlanPlaceMapping::getPlace) // PlanPlaceMapping에서 Place 객체를 가져옴
                    .map(PlaceInfo::of) // Place 객체를 PlaceInfo 객체로 변환
                    .collect(Collectors.toList());

            int favoriteCount = favoriteCountMap.getOrDefault(plan.getId(), 0);
            int bookmarkCount = bookmarkCountMap.getOrDefault(plan.getId(), 0);
            List<String> tagList = tagListMap.getOrDefault(plan.getId(), Collections.emptyList());

            return PlansInformation.builder()
                    .userId(plan.getUser().getId())
                    .userProfileUrl(plan.getUser().getProfileImageUrl())
                    .userProfileName(plan.getUser().getProfileName())
                    .planId(plan.getId())
                    .planName(plan.getName())
                    .placeInfoList(placeInfoList)
                    .favoriteCount(favoriteCount)
                    .bookmarkCount(bookmarkCount)
                    .tagList(tagList)
                    .build();
        }).collect(Collectors.toList());

        return result;
    }

    @Transactional
    public void deleteSharedPlan(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new DataNotFoundException()
        );
        User user = userRepository.findById(SecurityContextHolderUtil.getUserId()).orElseThrow(
                () -> new DataNotFoundException()
        );
        if(plan.getUser().getId() != user.getId()) {
            throw new DataNotFoundException();
        }

        plan.changeOpenness(false);
    }
}
