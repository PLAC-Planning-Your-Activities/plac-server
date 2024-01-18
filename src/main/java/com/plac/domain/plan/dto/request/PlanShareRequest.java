package com.plac.domain.plan.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanShareRequest {

    private List<@Min(1) @Max(13) Long> tagIds;

    private List<@Pattern(regexp = "^[\\p{IsHangul}\\d]*$", message = "태그는 한글과 숫자만 입력 가능합니다. (띄어쓰기 X)") String> etc;


    @Builder
    public PlanShareRequest(List<Long> tagIds, List<String> etc) {
        this.tagIds = tagIds;
        this.etc = etc;
    }
}
