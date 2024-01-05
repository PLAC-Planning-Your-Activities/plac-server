package com.plac.controller;

import com.plac.domain.plan.Plan;
import com.plac.dto.request.plan.CreatePlanReqDto;
import com.plac.dto.response.plan.PlanResDto;
import com.plac.service.plan.PlanService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    @PostMapping("")
    public ResponseEntity<?> createPlan(@RequestBody @Valid CreatePlanReqDto req) {
        Plan createdPlan = planService.createPlan(req);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("planId", createdPlan.getId());

        return MessageUtil.buildResponseEntity(responseData, HttpStatus.CREATED, "Plan created successfully");
    }

    @GetMapping("")
    public ResponseEntity<?> getPlans(
            @RequestParam(required = false) String beforePlanId,
            @RequestParam(required = false, defaultValue = "20") int pageSize

    ) {
        List<PlanResDto> responseData = planService.getPlans(beforePlanId, pageSize);

        return MessageUtil.buildResponseEntity(responseData, HttpStatus.OK, "success");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyPlans(
            @RequestParam(required = false) String beforePlanId,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ) {
        List<PlanResDto> responseData = planService.getUserPlans(beforePlanId, pageSize);

        return MessageUtil.buildResponseEntity(responseData, HttpStatus.OK, "success");
    }


    @GetMapping("/like")
    public ResponseEntity<?> getLikePlans(
            @RequestParam(required = false) String beforePlanId,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ) {
        List<PlanResDto> responseData = planService.getLikePlans(beforePlanId, pageSize);

        return MessageUtil.buildResponseEntity(responseData, HttpStatus.OK, "success");
    }

    @GetMapping("/scrap")
    public ResponseEntity<?> getScrapPlans(
            @RequestParam(required = false) String beforePlanId,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ) {
        List<PlanResDto> responseData = planService.getScrapPlans();

        return MessageUtil.buildResponseEntity(responseData, HttpStatus.OK, "success");
    }

    @PostMapping("{id}/like")
    public ResponseEntity<?> createPlanLike(@PathVariable(name = "id") String planId) {
        planService.createPlanLike(planId);

        return MessageUtil.buildResponseEntity(HttpStatus.CREATED, "PlanLike created successfully");
    }

    @DeleteMapping("{id}/like")
    public ResponseEntity<?> deletePlanLike(@PathVariable(name = "id") String planId) {
        planService.deletePlanLike(planId);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "PlanLike delete successfully");
    }

    @PostMapping("{id}/scrap")
    public ResponseEntity<?> createPlanScrap(@PathVariable(name = "id") String planId) {
        planService.createPlanScrap(planId);

        return MessageUtil.buildResponseEntity(HttpStatus.CREATED, "PlanScrap created successfully");
    }

    @DeleteMapping("{id}/scrap")
    public ResponseEntity<?> deletePlanScrap(@PathVariable(name = "id") String planId) {
        planService.deletePlanScrap(planId);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "PlanScrap delete successfully");
    }
}
