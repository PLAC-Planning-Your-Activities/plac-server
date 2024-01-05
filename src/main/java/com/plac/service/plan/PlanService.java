package com.plac.service.plan;

import com.plac.domain.User;
import com.plac.domain.destination.Destination;
import com.plac.domain.mappedenum.PlanTagType;
import com.plac.domain.plan.Plan;
import com.plac.domain.plan.PlanLike;
import com.plac.domain.plan.PlanScrap;
import com.plac.domain.plan.PlanTag;
import com.plac.dto.request.plan.CreatePlanReqDto;
import com.plac.dto.request.plan.PlanTagReqDto;
import com.plac.dto.response.place.PlaceResDto;
import com.plac.dto.response.plan.PlanResDto;
import com.plac.repository.plan.PlanLikeRepository;
import com.plac.repository.plan.PlanRepository;
import com.plac.repository.plan.PlanScrapRepository;
import com.plac.service.destination.DestinationService;
import com.plac.service.place.PlaceService;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlaceService placeService;
    private final DestinationService destinationService;

    private final PlanRepository planRepository;
    private final PlanLikeRepository planLikeRepository;
    private final PlanScrapRepository planScrapRepository;

    private final MongoTemplate mongoTemplate;

    public Plan createPlan(CreatePlanReqDto req) {
        User user = SecurityContextHolderUtil.getUser();

        validatePlaceIds(req.getPlaceIdList());

        Destination destination = destinationService.getDestination(req.getDestinationId());

        List<PlanTag> tags = convertToPlanTags(req.getTags());
        PlanTag ageTag = new PlanTag(PlanTagType.AGE, String.valueOf(SecurityContextHolderUtil.getUserAge()));
        tags.add(ageTag);

        Plan plan = Plan.builder()
                .userId(user.getId())
                .userNickname(user.getProfileName())
                .userProfileImageUrl(user.getProfileImageUrl())
                .destinationId(destination.getId())
                .destinationName(destination.getName())
                .thumbnailImageUrl(destination.getThumbnailUrl())
                .name(req.getName())
                .openStatus(req.getOpenStatus())
                .parentPlanId(req.getParentPlanId())
                .placeIdList(req.getPlaceIdList())
                .tags(tags)
                .likeCount(0)
                .scrapCount(0)
                .build();

        return planRepository.save(plan);
    }

    private void validatePlaceIds(List<Long> placeIds) {
        if (!placeService.placesExist(placeIds)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 placeId 입니다.");
        }
    }

    private List<PlanTag> convertToPlanTags(List<PlanTagReqDto> tagDtos) {
        if (tagDtos == null) {
            return Collections.emptyList();
        }
        return tagDtos.stream()
                .map(dto -> new PlanTag(dto.getType(), dto.getName()))
                .collect(Collectors.toList());
    }

    public List<PlanResDto> getPlans(String beforePlanId, int pageSize) {
        List<Plan> allPlans = planRepository.findAll();

        // TODO 임시 페이징 - 정렬순 구현시 수정하기
        int startIndex = 0;
        if (beforePlanId != null) {
            for (int i = 0; i < allPlans.size(); i++) {
                if (allPlans.get(i).getId().equals(beforePlanId)) {
                    startIndex = i + 1;
                    break;
                }
            }
        }
        int endIndex = startIndex + pageSize;
        if (endIndex > allPlans.size()) {
            endIndex = allPlans.size();
        }
        List<Plan> plans = allPlans.subList(startIndex, endIndex);
        // ----------

        Set<Long> allPlaceIds = plans.stream()
                .flatMap(plan -> plan.getPlaceIdList().stream())
                .collect(Collectors.toSet());

        Long userId = SecurityContextHolderUtil.getUserId();
        Set<String> likePlanIds = planLikeRepository.findByUserIdAndPlanIdIn(userId, plans.stream().map(Plan::getId).collect(Collectors.toList())).stream().map(PlanLike::getPlanId).collect(Collectors.toSet());
        Set<String> scrapPlanIds = planScrapRepository.findByUserIdAndPlanIdIn(userId, plans.stream().map(Plan::getId).collect(Collectors.toList())).stream().map(PlanScrap::getPlanId).collect(Collectors.toSet());

        List<PlaceResDto> placeResDtos = placeService.getPlaces(new ArrayList<>(allPlaceIds));
        Map<Long, PlaceResDto> placeResDtoMap = placeResDtos.stream()
                .collect(Collectors.toMap(p -> p.getPlacPlaceId(), p -> p));

        return plans.stream()
                .map(plan -> {
                    List<PlaceResDto> placesInPlan = plan.getPlaceIdList().stream()
                            .map(placeId -> placeResDtoMap.getOrDefault(placeId, null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    return PlanResDto.of(plan, placesInPlan, likePlanIds.contains(plan.getId()), scrapPlanIds.contains(plan.getId()));
                })
                .collect(Collectors.toList());
    }

    public List<PlanResDto> getUserPlans(String beforePlanId, int pageSize) {
        Long userId = SecurityContextHolderUtil.getUserId();

        List<Plan> plans = planRepository.findPlansByUserIdWithPaging(userId, beforePlanId, pageSize);

        Set<Long> allPlaceIds = plans.stream()
                .flatMap(plan -> plan.getPlaceIdList().stream())
                .collect(Collectors.toSet());

        Set<String> likePlanIds = planLikeRepository.findByUserIdAndPlanIdIn(userId, plans.stream().map(Plan::getId).collect(Collectors.toList())).stream().map(PlanLike::getPlanId).collect(Collectors.toSet());
        Set<String> scrapPlanIds = planScrapRepository.findByUserIdAndPlanIdIn(userId, plans.stream().map(Plan::getId).collect(Collectors.toList())).stream().map(PlanScrap::getPlanId).collect(Collectors.toSet());

        List<PlaceResDto> placeResDtos = placeService.getPlaces(new ArrayList<>(allPlaceIds));
        Map<Long, PlaceResDto> placeResDtoMap = placeResDtos.stream()
                .collect(Collectors.toMap(p -> p.getPlacPlaceId(), p -> p));

        return plans.stream()
                .map(plan -> {
                    List<PlaceResDto> placesInPlan = plan.getPlaceIdList().stream()
                            .map(placeId -> placeResDtoMap.getOrDefault(placeId, null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    return PlanResDto.of(plan, placesInPlan, likePlanIds.contains(plan.getId()), scrapPlanIds.contains(plan.getId()));
                })
                .collect(Collectors.toList());
    }

    public List<PlanResDto> getLikePlans(String beforePlanId, int pageSize) {
        Long userId = SecurityContextHolderUtil.getUserId();
        List<String> likePlanIds = planLikeRepository.findPlanLikesByUserIdWithPaging(userId, beforePlanId, pageSize).stream()
                .map(PlanLike::getPlanId)
                .collect(Collectors.toList());

        if (likePlanIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Plan> plans = planRepository.findByIdIn(likePlanIds);
        plans.sort(Comparator.comparing(p -> likePlanIds.indexOf(p.getId())));

        Set<Long> allPlaceIds = plans.stream()
                .flatMap(plan -> plan.getPlaceIdList().stream())
                .collect(Collectors.toSet());

        Set<String> scrapPlanIds = planScrapRepository.findByUserIdAndPlanIdIn(userId, plans.stream().map(Plan::getId).collect(Collectors.toList())).stream().map(PlanScrap::getPlanId).collect(Collectors.toSet());

        List<PlaceResDto> placeResDtos = placeService.getPlaces(new ArrayList<>(allPlaceIds));
        Map<Long, PlaceResDto> placeResDtoMap = placeResDtos.stream()
                .collect(Collectors.toMap(p -> p.getPlacPlaceId(), p -> p));

        return plans.stream()
                .map(plan -> {
                    List<PlaceResDto> placesInPlan = plan.getPlaceIdList().stream()
                            .map(placeId -> placeResDtoMap.getOrDefault(placeId, null)) // placeId에 해당하는 PlaceResDto를 찾거나 없으면 null 반환
                            .filter(Objects::nonNull) // null 값 제거 // TODO default가 발생하면 안되는데 어떻게 할지 고민됨
                            .collect(Collectors.toList());

                    return PlanResDto.of(plan, placesInPlan, true, scrapPlanIds.contains(plan.getId()));
                })
                .collect(Collectors.toList());
    }

    public List<PlanResDto> getScrapPlans() {
        Long userId = SecurityContextHolderUtil.getUserId();
        Set<String> scrapPlanIds = planScrapRepository.findByUserId(userId).stream()
                .map(PlanScrap::getPlanId)
                .collect(Collectors.toSet());

        if (scrapPlanIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Plan> plans = planRepository.findByIdIn(scrapPlanIds);

        Set<Long> allPlaceIds = plans.stream()
                .flatMap(plan -> plan.getPlaceIdList().stream())
                .collect(Collectors.toSet());

        Set<String> likePlanIds = planLikeRepository.findByUserIdAndPlanIdIn(userId, plans.stream().map(Plan::getId).collect(Collectors.toList())).stream().map(PlanLike::getPlanId).collect(Collectors.toSet());

        List<PlaceResDto> placeResDtos = placeService.getPlaces(new ArrayList<>(allPlaceIds));
        Map<Long, PlaceResDto> placeResDtoMap = placeResDtos.stream()
                .collect(Collectors.toMap(p -> p.getPlacPlaceId(), p -> p));

        return plans.stream()
                .map(plan -> {
                    List<PlaceResDto> placesInPlan = plan.getPlaceIdList().stream()
                            .map(placeId -> placeResDtoMap.getOrDefault(placeId, null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    return PlanResDto.of(plan, placesInPlan, likePlanIds.contains(plan.getId()), true);
                })
                .collect(Collectors.toList());

    }

    // 유사한 기능을 수행하는 메서드를 위한 private helper 메서드
    private void updatePlanCounter(String planId, String field, int incrementValue) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(planId)));
        Update update = new Update().inc(field, incrementValue);
        mongoTemplate.findAndModify(query, update, Plan.class);
    }

    public void createPlanLike(String planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        if (planLikeRepository.existsByPlanIdAndUserId(planId, userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already liked this plan");
        }
        PlanLike planThumb = new PlanLike(planId, userId);
        planLikeRepository.save(planThumb);

        updatePlanCounter(planId, "likeCount", 1);
    }

    public void deletePlanLike(String planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        Optional<PlanLike> planLikeOptional = planLikeRepository.findByPlanIdAndUserId(planId, userId);
        if (!planLikeOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not liked this plan");
        }
        planLikeRepository.delete(planLikeOptional.get());

        updatePlanCounter(planId, "likeCount", -1);
    }

    public void createPlanScrap(String planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        if (planScrapRepository.existsByPlanIdAndUserId(planId, userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already scrapped this plan");
        }
        PlanScrap planScrap = new PlanScrap(planId, userId);
        planScrapRepository.save(planScrap);

        updatePlanCounter(planId, "scrapCount", 1);
    }

    public void deletePlanScrap(String planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        Optional<PlanScrap> planScrapOptional = planScrapRepository.findByPlanIdAndUserId(planId, userId);
        if (!planScrapOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has not scrapped this plan");
        }
        planScrapRepository.delete(planScrapOptional.get());

        updatePlanCounter(planId, "scrapCount", -1);
    }
}