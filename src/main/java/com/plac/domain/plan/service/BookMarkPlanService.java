package com.plac.domain.plan.service;

import com.plac.domain.plan.dto.response.MyListPlan;
import com.plac.domain.plan.entity.BookmarkPlan;
import com.plac.domain.plan.entity.Plan;
import com.plac.domain.plan.repository.BookmarkPlanRepository;
import com.plac.domain.plan.repository.PlanRepository;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.common.DataNotFoundException;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookMarkPlanService {

    private final BookmarkPlanRepository bookmarkPlanRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    public void createBookMarkPlan(Long planId) {
        Long userId = SecurityContextHolderUtil.getUserId();

        User user = userRepository.findById(userId).orElseThrow(
                () -> new DataNotFoundException());

        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new DataNotFoundException());

        BookmarkPlan bookmarkPlan = BookmarkPlan.builder()
                .user(user)
                .plan(plan)
                .build();

        bookmarkPlanRepository.save(bookmarkPlan);
    }

    public List<MyListPlan> getMyBookMarkPlans() {
        List<BookmarkPlan> bookmarkPlans = bookmarkPlanRepository.findByUserId(
                SecurityContextHolderUtil.getUserId());

        List<Long> planIds = bookmarkPlans.stream()
                .map(bookmarkPlan -> bookmarkPlan.getPlan().getId())
                .collect(Collectors.toList());

        List<Plan> plans = planRepository.findByIdIn(planIds);

        // plan 객체를 Id 기준으로 Map으로 변환
        Map<Long, Plan> planMap = plans.stream()
                .collect(Collectors.toMap(Plan::getId, Function.identity()));

        List<MyListPlan> result = new ArrayList<>();

        for (BookmarkPlan bookmarkPlan : bookmarkPlans) {
            Plan plan = planMap.get(bookmarkPlan.getPlan().getId());

            if (plan != null){
                MyListPlan myListPlan = MyListPlan.builder()
                        .planId(plan.getId())
                        .planName(plan.getName())
                        .destinationName(plan.getDestinationName())
                        .imageUrl(plan.getImageUrl())
                        .myPlan(false)
                        .build();

                result.add(myListPlan);
            }
        }
        return result;
    }
}
