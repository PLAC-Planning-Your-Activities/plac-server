package com.plac.dto.request.plan;

import com.plac.domain.mappedenum.PlanTagType;
import lombok.Getter;

@Getter
public class PlanTagReqDto {
    private PlanTagType type;
    private String name;
}
