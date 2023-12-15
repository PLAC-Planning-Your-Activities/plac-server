package com.plac.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
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

    private Long kakaoPlaceId;

    private String name;

    private String thumbnailImageUrl;

    private String streetNameAddress;

    @Column(precision=11, scale=8)
    @Comment("장소의 경도 : longitude")
    private BigDecimal x;

    @Column(precision=11, scale=8)
    @Comment("장소의 위도 : latitude")
    private BigDecimal y;

}
