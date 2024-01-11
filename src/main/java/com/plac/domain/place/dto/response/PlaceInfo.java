package com.plac.domain.place.dto.response;

import com.plac.domain.place.entity.Place;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInfo {
    private String placeName;
    private String type;
    private Long kakaoPlaceId;
    private String imageUrl;
    private BigDecimal x;
    private BigDecimal y;
    private String address;
    private int page;

    public static PlaceInfo of(Place place){
        return PlaceInfo.builder()
                .placeName(place.getPlaceName())
                .type(place.getType())
                .kakaoPlaceId(place.getKakaoPlaceId())
                .imageUrl(place.getThumbnailImageUrl())
                .x(place.getX())
                .y(place.getY())
                .address(place.getStreetNameAddress())
                .build();
    }
}
