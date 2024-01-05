package com.plac.dto.response.place;

import com.plac.domain.Place;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceResDto {
    private Long kakaoPlaceId;
    private Long placPlaceId;
    private String placeName;
    private String thumbnailImageUrl;
    private String streetNameAddress;
    private float totalRating;
    private int reviewCount;
    private BigDecimal x;
    private BigDecimal y;

    private PlaceResDto(Place place, float totalRating, int reviewCount) {
        this.kakaoPlaceId = place.getKakaoPlaceId();
        this.placPlaceId = place.getId();
        this.placeName = place.getPlaceName();
        this.thumbnailImageUrl = place.getThumbnailImageUrl();
        this.streetNameAddress = place.getStreetNameAddress();
        this.totalRating = totalRating;
        this.reviewCount = reviewCount;
        this.x = place.getX();
        this.y = place.getY();
    }

    public static PlaceResDto of(Place place, float totalRating, int reviewCount) {
        return new PlaceResDto(place, totalRating, reviewCount);
    }
}
