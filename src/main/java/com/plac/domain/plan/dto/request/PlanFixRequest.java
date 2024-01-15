package com.plac.domain.plan.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanFixRequest {
    private List<Long> kakaoPlaceIdList;

    public PlanFixRequest(List<Long> kakaoPlaceIdList) {
        this.kakaoPlaceIdList = kakaoPlaceIdList;
    }
}
