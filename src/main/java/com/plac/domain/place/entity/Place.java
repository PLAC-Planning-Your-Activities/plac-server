package com.plac.domain.place.entity;

import com.plac.common.AbstractTimeEntity;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "place")
public class Place extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String type;

    private Long kakaoPlaceId;

    private String placeName;

    private String thumbnailImageUrl;

    private String streetNameAddress;

    @Column(precision=11, scale=8)
    @Comment("장소의 경도 : longitude")
    private BigDecimal x;

    @Column(precision=11, scale=8)
    @Comment("장소의 위도 : latitude")
    private BigDecimal y;

    public static Place create(String type, Long kakaoPlaceId, String placeName, String thumbnailImageUrl,
                               String streetNameAddress, BigDecimal x, BigDecimal y) {
        return Place.builder()
                .type(type)
                .kakaoPlaceId(kakaoPlaceId)
                .placeName(placeName)
                .thumbnailImageUrl(thumbnailImageUrl)
                .streetNameAddress(streetNameAddress)
                .x(x)
                .y(y)
                .build();
    }
}
