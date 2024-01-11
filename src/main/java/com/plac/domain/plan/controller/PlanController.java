package com.plac.domain.plan.controller;

import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.request.PlanShareRequest;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.service.PlanService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    @PostMapping("")
    public ResponseEntity<?> createPlan(@RequestBody PlanCreateRequest planRequest){
        PlanCreateResponse result = planService.createPlan(planRequest);

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

    @PostMapping("")
    public ResponseEntity<?> sharePlanToCommunity(
            @RequestBody PlanShareRequest planRequest,
            @PathVariable("planId") Long planId
    ){
        planService.sharePlanToCommunity(planRequest, planId);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

}
