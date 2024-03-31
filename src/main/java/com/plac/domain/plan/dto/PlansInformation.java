package com.plac.domain.plan.dto;

import com.plac.domain.place.dto.response.PlaceInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlansInformation {
    private Long planId;
    private String userProfileName;
    private String profileImageUrl;
    private long minuteDifferences;
    private String planName;
    private long favoriteCount;
    private long bookmarkCount;
    private boolean isFavorite;
    private boolean isBookmarked;
    private List<String> tagList;
    private List<PlaceInfo> placeInfoList;
}
