package com.plac.domain.plan.dto.response;

import com.plac.domain.place.dto.response.PlaceInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlansInformation {
    private String userProfileName;
    private String profileImageUrl;
    private long minuteDifferences;
    private String planName;
    private int favoriteCount;
    private int bookmarkCount;
    private List<PlaceInfo> placeInfoList;
}
