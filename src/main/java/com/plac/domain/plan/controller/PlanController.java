package com.plac.domain.plan.controller;

import com.plac.domain.plan.dto.PlansInformation;
import com.plac.domain.plan.dto.request.CommunityPlanRequest;
import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanFixRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.response.GetMyListPlansResponseDto;
import com.plac.domain.plan.dto.response.GetPlanPlaceResponseDto;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<?> createPlan(@RequestBody PlanCreateRequest planRequest){
        PlanCreateResponse result = planService.createPlan(planRequest);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/community/{planId}")
    public ResponseEntity<?> sharePlanToCommunity(
            @PathVariable("planId") Long planId,
            @Valid @RequestBody PlanShareRequest planRequest
    ){
        planService.sharePlanToCommunity(planRequest, planId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/community/{planId}")
    public ResponseEntity<?> deleteMySharedPlan (
            @PathVariable("planId") Long planId
    ){
        planService.deleteMySharedPlan(planId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<?> fixPlan(
            @PathVariable("planId") Long planId,
            @RequestBody PlanFixRequest planRequest
    ){
        planService.fixPlan(planRequest, planId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable("planId") Long planId){
        planService.deletePlan(planId);
        return ResponseEntity.ok().build();
    }

    /**
     * 플랜 좋아요 누르기 */
    @PostMapping("/{planId}/likes")
    public ResponseEntity<?> createFavoritePlan(
            @PathVariable("planId") Long planId
    ){
        planService.makeFavoritePlan(planId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{planId}/likes")
    public ResponseEntity<?> clearFavoritePlan(@PathVariable("planId") Long planId) {
        planService.clearFavoritePlan(planId);
        return ResponseEntity.ok().build();
    }

    /**
     * 플랜 마이리스트 저장 */
    @PostMapping("/{planId}/my-list")
    public ResponseEntity<Void> addMyListDibsPlan(@PathVariable Long planId) {
        planService.triggerDibsMyListPlan(planId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-list/{planId}/details")
    public ResponseEntity<List<GetPlanPlaceResponseDto>> getMyListDibsPlanOfPlaces(@PathVariable Long planId) {
        List<GetPlanPlaceResponseDto> response = planService.findMyListPlanPlaceByPlan(planId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-list")
    public ResponseEntity<List<GetMyListPlansResponseDto>> getMyListDibsPlan() {
        List<GetMyListPlansResponseDto> response = planService.findMyListPlans();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/community")
    public ResponseEntity<?> getCommunityPlans(
            @ModelAttribute CommunityPlanRequest planRequest
    ){
        List<PlansInformation> result = planService.getCommunityPlans(planRequest);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/most-favorites")
    public ResponseEntity<?> getMostPopularPlans(){
        List<PlansInformation> result = planService.getMostPopularPlans();
        return ResponseEntity.ok().body(result);
    }
}
