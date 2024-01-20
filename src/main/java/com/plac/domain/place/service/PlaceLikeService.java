package com.plac.domain.place.service;

import com.plac.domain.place.dto.request.CreatePlaceLikeRequest;
import com.plac.domain.place.entity.PlaceLike;
import com.plac.domain.place.repository.PlaceLikeRepository;
import com.plac.domain.place.repository.PlaceRepository;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.common.DataNotFoundException;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceLikeService {

    private final UserRepository userRepository;
    private final PlaceLikeRepository placeLikeRepository;
    private final PlaceRepository placeRepository;

    public Long createPlaceLike(CreatePlaceLikeRequest placeLikeRequest) {
        List<Long> kakaoPlaceIds = placeLikeRequest.getKakaoPlaceIdList();

        User user = userRepository.findById(SecurityContextHolderUtil.getUserId())
                .orElseThrow(() -> new DataNotFoundException());

        for (Long kakaoPlaceId : kakaoPlaceIds) {
            PlaceLike placeLike = placeLikeRepository.save(
                    PlaceLike.builder()
                            .user(user)
                            .kakaoPlaceId(kakaoPlaceId)
                            .build()
            );
        }

        return 1L;
    }
}
