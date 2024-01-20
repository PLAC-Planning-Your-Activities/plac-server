package com.plac.domain.plan.controller;

import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.request.TagListRequest;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.dto.response.PlansInformation;
import com.plac.domain.plan.service.BookMarkPlanService;
import com.plac.domain.plan.service.PlanService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;
    private final BookMarkPlanService bookMarkPlanService;

    @PostMapping("")
    public ResponseEntity<?> createPlan(@RequestBody PlanCreateRequest planRequest) {
        PlanCreateResponse result = planService.createPlan(planRequest);
        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<?> deletePlan(
            @PathVariable("planId") Long planId,
            @RequestParam("myPlan") boolean myPlan
    ) {
        planService.deletePlan(planId, myPlan);
        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @PostMapping("/{planId}/community")
    public ResponseEntity<?> sharePlanToCommunity(
            @PathVariable("planId") Long planId,
            @Valid @RequestBody PlanShareRequest planRequest
    ){
        planService.sharePlanToCommunity(planRequest, planId);
        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @PostMapping("/{planId}/likes")
    public ResponseEntity<?> createFavoritePlan(
            @PathVariable("planId") Long planId
    ){
        planService.makeFavoritePlan(planId);
        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @DeleteMapping("/{planId}/likes")
    public ResponseEntity<?> clearFavoritePlan(@PathVariable("planId") Long planId){
        planService.clearFavoritePlan(planId);
        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    // 다른 유저의 플랜 저장 (마이페이지에서 확인 가능)
    @PostMapping("/bookmarks/{planId}")
    public ResponseEntity<?> createBookMarkPlan(@PathVariable("planId") Long planId){
        planService.saveBookMarkPlan(planId);
        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @DeleteMapping("/bookmarks/{planId}")
    public ResponseEntity<?> deleteBookMarkPlan(@PathVariable("planId") Long planId){
        planService.deleteBookMarkPlan(planId);
        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @GetMapping("/destinations")
    public ResponseEntity<?> getPlansByDestination(
            @RequestParam(name = "destinationName", required = true) String destinationName,
            @RequestParam(name = "sortBy", required = false, defaultValue = "최신순") String sortBy,
            @RequestParam(name = "placeName", required = false, defaultValue = "NONE") String placeName,
            @RequestBody TagListRequest tagListRequest
            ){
        List<PlansInformation> result = planService.getPlansByDestinations(destinationName, sortBy, tagListRequest, placeName);
        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

    @GetMapping("/most-favorites")
    public ResponseEntity<?> getMostPopularPlans(){
        List<PlansInformation> result = planService.getMostPopularPlans();

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

}
