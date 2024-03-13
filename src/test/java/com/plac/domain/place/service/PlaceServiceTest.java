package com.plac.domain.place.service;

import com.plac.domain.place.dto.request.CreatePlaceRequest;
import com.plac.domain.place.entity.Place;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PlaceServiceTest {

    @Autowired
    private PlaceService placeService;

    @Test
    @DisplayName("장소 DB에 추가")
    void createPlace() {
        CreatePlaceRequest request = new CreatePlaceRequest(1L, "test-place",
                null, null, BigDecimal.ZERO, BigDecimal.ZERO);

        Place place = placeService.createPlace(request);
        assertThat(place.getId()).isEqualTo(1L);
        assertThat(place.getKakaoPlaceId()).isEqualTo(1L);
        assertThat(place.getPlaceName()).isEqualTo("test-place");
    }

}