package com.plac.dto.response.plan;

import com.plac.domain.plan.Plan;
import com.plac.domain.plan.PlanTag;
import com.plac.dto.response.place.PlaceResDto;
import lombok.Getter;

import java.util.List;

@Getter
public class PlanResDto {
    String planId;
    String planName;
    Long userId;
    String userNickName;
    String userProfileImageUrl;
    boolean openStatus;
    List<PlanTag> planTags;
    List<PlaceResDto> places;
    int likeCount;
    int scrapCount;
    boolean userLikeStatus;
    boolean userScrapStatus;

    private PlanResDto(Plan plan, List<PlaceResDto> places, boolean userLikeStatus, boolean userScrapStatus) {
        this.planId = plan.getId();
        this.userId = plan.getUserId();
        this.userNickName = plan.getUserNickname();
        this.userProfileImageUrl = plan.getUserProfileImageUrl();
        this.likeCount = plan.getLikeCount();
        this.scrapCount = plan.getScrapCount();
        this.planName = plan.getName();
        this.planTags = plan.getTags();
        this.places = places;
        this.userLikeStatus = userLikeStatus;
        this.userScrapStatus = userScrapStatus;
    }

    public static PlanResDto of(Plan plan, List<PlaceResDto> places, boolean userLikeStatus, boolean userScrapStatus) {
        return new PlanResDto(plan, places, userLikeStatus, userScrapStatus);
    }
}
