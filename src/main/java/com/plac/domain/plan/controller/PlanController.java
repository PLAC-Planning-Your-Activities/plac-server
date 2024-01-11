package com.plac.domain.plan.controller;

import com.plac.domain.plan.dto.request.PlanCreateRequest;
import com.plac.domain.plan.dto.response.PlanCreateResponse;
import com.plac.domain.plan.service.PlanService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    @PostMapping("")
    public ResponseEntity<?> createPlan(PlanCreateRequest planRequest){
        PlanCreateResponse result = planService.createPlan(planRequest);

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

}
