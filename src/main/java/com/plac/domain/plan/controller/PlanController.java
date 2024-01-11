package com.plac.domain.plan.controller;

import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanFixRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.service.PlanService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    @PostMapping("")
    public ResponseEntity<?> createPlan(@RequestBody PlanCreateRequest planRequest){
        PlanCreateResponse result = planService.createPlan(planRequest);
        System.out.println("result.getPlanId() = " + result.getPlanId());

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

    @PostMapping("/community/{planId}")
    public ResponseEntity<?> sharePlanToCommunity(
            @PathVariable("planId") Long planId,
            @Valid @RequestBody PlanShareRequest planRequest
    ){
        planService.sharePlanToCommunity(planRequest, planId);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @PatchMapping("")
    public ResponseEntity<?> fixPlan(
            @PathVariable("planId") Long planId,
            @RequestBody PlanFixRequest planRequest
    ){
        planService.fixPlan(planRequest, planId);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable("planId") Long planId){
        planService.deletePlan(planId);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

}
