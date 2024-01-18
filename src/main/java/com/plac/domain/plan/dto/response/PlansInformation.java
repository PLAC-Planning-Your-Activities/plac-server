package com.plac.domain.plan.dto.response;

import com.plac.domain.place.dto.response.PlaceInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlansInformation {
    private Long userId;
    private String userProfileUrl;
    private String userProfileName;
    private int favoriteCount;
    private int bookmarkCount;
    private List<String> tagList;
    private Long minuteDifferences;
    private Long planId;
    private String planName;
    private List<PlaceInfo> placeInfoList;
}
