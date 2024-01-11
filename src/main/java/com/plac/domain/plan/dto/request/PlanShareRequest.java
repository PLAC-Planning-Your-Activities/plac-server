package com.plac.domain.plan.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanShareRequest {

    private List<Long> tagIds;

    private List<String> etc;

    @Builder
    public PlanShareRequest(List<Long> tagIds, List<String> etc) {
        this.tagIds = tagIds;
        this.etc = etc;
    }
}
