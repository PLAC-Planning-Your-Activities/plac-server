package com.plac.domain.plan.service;

import com.plac.domain.place.dto.response.PlaceInfo;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.repository.PlaceRepository;
import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.repository.PlanRepository;
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

    List<Long> placeIdList = new ArrayList<>();

    public PlanCreateResponse createPlan(PlanCreateRequest planRequest) {
        Long userId = SecurityContextHolderUtil.getUserId();

        Optional<User> userOpt = userRepository.findById(userId);

        if (!userOpt.isPresent()) {
            throw new UserPrincipalNotFoundException("no user");
        }
        User user = userOpt.get();
        List<PlaceInfo> placeList = planRequest.getPlaceList();

        for (PlaceInfo placeInfo : placeList) {
            placeRepository.findByKakaoPlaceId(placeInfo.getKakaoPlaceId())
                    .ifPresent(place -> placeIdList.add(createAndSavePlace(place)));
        }

        Plan savedPlan = createNewPlan(planRequest, user);
        return new PlanCreateResponse(savedPlan.getId());
    }

    private Plan createNewPlan(PlanCreateRequest planRequest, User user) {
        Plan plan = Plan.builder()
                .destinationName(planRequest.getDestinationName())
                .name(planRequest.getPlanName())
                .user(user)
                .open(false)
                .build();

        Plan savedPlan = planRepository.save(plan);

        return savedPlan;
    }

    private Long createAndSavePlace(Place place) {
        Place newPlace = Place.builder()
                .placeName(place.getPlaceName())
                .type(place.getType())
                .kakaoPlaceId(place.getKakaoPlaceId())
                .x(place.getX())
                .y(place.getY())
                .thumbnailImageUrl(place.getThumbnailImageUrl())
                .streetNameAddress(place.getStreetNameAddress())
                .build();

        Place savePlace = placeRepository.save(newPlace);

        return savePlace.getId();
    }

    @Transactional
    public void sharePlanToCommunity(PlanShareRequest planRequest, Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("존재하지 않는 planId 입니다.")
        );

        plan.setOpen(true);
    }
}
