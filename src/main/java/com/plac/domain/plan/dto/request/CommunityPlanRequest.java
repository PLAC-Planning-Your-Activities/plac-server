package com.plac.domain.plan.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CommunityPlanRequest {

    @NonNull
    private String destination;
    private String place;
    @NonNull
    private String sortBy;
    private Integer ageRange;
    private String gender;
    private String tags;
}
