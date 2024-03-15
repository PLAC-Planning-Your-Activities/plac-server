package com.plac.domain.place.repository.place;

import com.config.TestQueryDSLConfiguration;
import com.plac.domain.place.entity.Place;
import com.plac.domain.place.entity.PlaceDibs;
import com.plac.domain.place.repository.placeDibs.PlaceDibsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import({
//        PlaceRepository.class,
//        PlaceQueryRepository.class,
        PlaceQueryRepositoryImpl.class,
        TestQueryDSLConfiguration.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlaceQueryRepositoryImplTest {

    @Autowired
    private PlaceDibsRepository placeDibsRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    void test() {
        // given
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Place place = Place.builder()
                    .kakaoPlaceId(1L + i)
                    .placeName((i + 1) + "test-place-name")
                    .build();

            places.add(place);
        }
        placeRepository.saveAll(places);

        long userId = 1L;
        long userId2 = 2L;

        List<PlaceDibs> user1Dibs = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            PlaceDibs dibs = PlaceDibs.builder()
                    .userId(userId)
                    .kakaoPlaceId(places.get(i).getKakaoPlaceId())
                    .build();
            user1Dibs.add(dibs);
        }
        placeDibsRepository.saveAll(user1Dibs);

        List<PlaceDibs> user2Dibs = new ArrayList<>();
        for (int i = 2; i < 6; i++) {
            PlaceDibs dibs = PlaceDibs.builder()
                    .userId(userId2)
                    .kakaoPlaceId(places.get(i).getKakaoPlaceId())
                    .build();
            user2Dibs.add(dibs);
        }
        placeDibsRepository.saveAll(user2Dibs);

        // when
        List<Place> resultUser1 = placeRepository.findPlaceDibsByUserId(userId);
        List<Place> resultUser2 = placeRepository.findPlaceDibsByUserId(userId2);

        // then
        assertThat(resultUser1.size()).isEqualTo(10);
        assertThat(resultUser2.size()).isEqualTo(4);
    }
}