package com.plac.domain.plan.service;

import com.plac.domain.place.dto.response.PlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.repository.PlaceRepository;
import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanFixRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.entity.PlanPlaceMapping;
import com.plac.domain.plan.entity.PlanTag;
import com.plac.domain.plan.entity.PlanTagMapping;
import com.plac.domain.plan.repository.PlanPlaceMappingRepository;
import com.plac.domain.plan.repository.PlanRepository;
import com.plac.domain.plan.repository.PlanTagMappingRepository;
import com.plac.domain.plan.repository.PlanTagRepository;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.plan.PlanNotFoundException;
import com.plac.exception.user.UserPrincipalNotFoundException;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlaceRepository placeRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanPlaceMappingRepository planPlaceMappingRepository;
    private final PlanTagRepository planTagRepository;
    private final PlanTagMappingRepository planTagMappingRepository;

    @Transactional
    public PlanCreateResponse createPlan(PlanCreateRequest planRequest) {
        Long userId = SecurityContextHolderUtil.getUserId();
        Optional<User> userOpt = userRepository.findById(userId);

        if (!userOpt.isPresent()) {
            throw new UserPrincipalNotFoundException("no user");
        }
        User user = userOpt.get();
        List<Place> placeList = new ArrayList<>();

        for (PlaceInfo placeInfo : planRequest.getPlaceList()) {
            Optional<Place> placeOpt = placeRepository.findByKakaoPlaceId(placeInfo.getKakaoPlaceId());

            if (!placeOpt.isPresent())
                placeList.add(createAndSavePlace(placeInfo));
            else
                placeList.add(placeOpt.get());
        }
        Plan savedPlan = createNewPlan(planRequest, user, placeList);

        return new PlanCreateResponse(savedPlan.getId());
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
        System.out.println("planRequest.getDestinationName() = " + planRequest.getDestinationName());
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
}
