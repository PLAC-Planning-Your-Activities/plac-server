package com.plac.domain.plan.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyListPlan {
    private Long planId;
    private String planName;
    private String destinationName;
    private String imageUrl;
    private boolean myPlan;
}
