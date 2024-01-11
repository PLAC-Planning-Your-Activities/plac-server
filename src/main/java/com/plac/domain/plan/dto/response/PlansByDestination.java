package com.plac.domain.plan.dto.response;

import com.plac.domain.place.dto.response.PlaceInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlansByDestination {
    private String userProfileName;
    private long minuteDifferences;
    private String planName;
    private List<PlaceInfo> placeInfoList;
    private int favoriteCount;
    private int bookmarkCount;
}
