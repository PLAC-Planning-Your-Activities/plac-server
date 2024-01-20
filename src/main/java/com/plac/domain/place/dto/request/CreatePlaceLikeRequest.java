package com.plac.domain.place.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePlaceLikeRequest {
    List<Long> kakaoPlaceIdList;
}
