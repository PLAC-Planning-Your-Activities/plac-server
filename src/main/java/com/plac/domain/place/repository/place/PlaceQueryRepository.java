package com.plac.domain.place.repository.place;

import com.plac.domain.place.entity.Place;

import java.util.List;

public interface PlaceQueryRepository {

    List<Place> findPlaceDibsByUserId(long userId);
}
