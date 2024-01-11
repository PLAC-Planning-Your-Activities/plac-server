package com.plac.domain.plan.dto.request;

import com.plac.domain.place.dto.response.PlaceInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanCreateRequest {
    private String planName;
    private List<PlaceInfo> placeList;
    private String destinationName;

    @Builder
    public PlanCreateRequest(String planName, List<PlaceInfo> placeList, String destinationName) {
        this.planName = planName;
        this.placeList = placeList;
        this.destinationName = destinationName;
    }
}
