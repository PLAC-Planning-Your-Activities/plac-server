package com.plac.domain.place.dto.response;

import com.plac.domain.place.entity.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class GetMyListPlacesResponseDto {
    private String type;
    private Long kakaoPlaceId;
    private String placeName;
    private String thumbnailImageUrl;
    private String streetNameAddress;
    private BigDecimal x;
    private BigDecimal y;

    public GetMyListPlacesResponseDto(Place place) {
        this.type = place.getType();
        this.kakaoPlaceId = place.getKakaoPlaceId();
        this.placeName = place.getPlaceName();
        this.thumbnailImageUrl = place.getThumbnailImageUrl();
        this.streetNameAddress = place.getStreetNameAddress();
        this.x = place.getX();
        this.y = place.getY();
    }
}
