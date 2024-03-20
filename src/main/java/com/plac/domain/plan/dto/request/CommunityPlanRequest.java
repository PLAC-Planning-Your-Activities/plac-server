package com.plac.domain.plan.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommunityPlanRequest {

    @NonNull
    private String destinationName;
    private String placeName;
    @NonNull
    private String sortBy;
    private String ageRange;
    private String gender;
    private List<String> tagList;
}
