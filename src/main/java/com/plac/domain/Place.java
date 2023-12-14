package com.plac.domain;

import com.plac.domain.AbstractTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private BigDecimal latitude;

    @Column(precision=11, scale=8)
    private BigDecimal longitude;

}
