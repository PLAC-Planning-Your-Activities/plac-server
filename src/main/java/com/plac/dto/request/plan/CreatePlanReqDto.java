package com.plac.dto.request.plan;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
public class CreatePlanReqDto {
    @NonNull
    private String name;
    @NotEmpty
    private List<Long> placeIdList;
    @NotNull
    private String thumbnailImageUrl;
    @NotNull
    private String destinationId;
    @NonNull
    private Boolean openStatus = false;
    private Long parentPlanId = -1L;
    private List<PlanTagReqDto> tags;
}
