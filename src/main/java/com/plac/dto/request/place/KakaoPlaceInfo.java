package com.plac.dto.request.place;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPlaceInfo {
    private Long kakaoPlaceId;
    private BigDecimal x;
    private BigDecimal y;
}
