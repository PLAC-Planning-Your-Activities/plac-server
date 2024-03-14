package com.plac.domain.place.entity;

import com.plac.common.AbstractTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceDibs extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long kakaoPlaceId;

    @NotNull
    private Long userId;

    @Builder
    public PlaceDibs(Long id, Long kakaoPlaceId, Long userId) {
        this.id = id;
        this.kakaoPlaceId = kakaoPlaceId;
        this.userId = userId;
    }

    public static PlaceDibs create(Long kakaoPlaceId, Long userId) {
        return PlaceDibs
                .builder()
                .kakaoPlaceId(kakaoPlaceId)
                .userId(userId)
                .build();
    }
}
