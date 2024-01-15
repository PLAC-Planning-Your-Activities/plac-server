package com.plac.domain.plan.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanCreateResponse {
    private Long planId;

    @Builder
    public PlanCreateResponse(Long planId) {
        this.planId = planId;
    }
}
