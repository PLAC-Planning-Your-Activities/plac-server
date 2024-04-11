package com.plac.domain.plan.dto.response;

import com.plac.domain.plan.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetMyListPlansResponseDto {
    private String userProfileName;
    private Long planId;
    private String planName;
    private String destinationName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> placeImagesUrls;

    public GetMyListPlansResponseDto(String userProfileName, Plan plan, List<String> placeImagesUrls) {
        this.userProfileName = userProfileName;
        this.planId = plan.getId();
        this.planName = plan.getName();
        this.destinationName = plan.getDestinationName();
        this.createdAt = plan.getCreatedAt();
        this.updatedAt = plan.getUpdatedAt();
        this.placeImagesUrls = placeImagesUrls;
    }
}
