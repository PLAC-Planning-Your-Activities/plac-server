package com.plac.domain.plan.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class PlanFixRequest {
    private List<Long> kakaoPlaceIdList;

    public PlanFixRequest(List<Long> kakaoPlaceIdList) {
        this.kakaoPlaceIdList = kakaoPlaceIdList;
    }
}
