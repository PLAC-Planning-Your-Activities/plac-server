package com.plac.domain.mylist;

import com.plac.domain.plan.dto.response.MyListPlan;
import com.plac.domain.plan.service.BookMarkPlanService;
import com.plac.domain.plan.service.PlanService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-list")
public class MyListController {

    private final PlanService planService;
    private final BookMarkPlanService bookMarkPlanService;

    @GetMapping("/plans")
    public ResponseEntity<?> getMyListPlans(){
        List<MyListPlan> myPlans = planService.getMyPlans();
        List<MyListPlan> myBookMarkPlans = bookMarkPlanService.getMyBookMarkPlans();

        List<MyListPlan> result = new ArrayList<>(myPlans);
        result.addAll(myBookMarkPlans);

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

    @GetMapping("/place")
    public ResponseEntity<?> getMyListPlaces(){

    }
}
