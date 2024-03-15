package com.plac.domain.place.controller;

import com.plac.domain.place.dto.request.CreatePlacesRequest;
import com.plac.domain.place.dto.request.KakaoPlaceInfo;
import com.plac.domain.place.dto.response.GetMyListPlacesResponseDto;
import com.plac.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<?> createPlaceInfo(@RequestBody CreatePlacesRequest placeRequest) throws Exception {
        placeService.createPlaces(placeRequest);
        return ResponseEntity.ok().build();
    }

    // TODO : API 네이밍 변경 가능, 조회 추가 작업
    @GetMapping("/my-list")
    public ResponseEntity<List<GetMyListPlacesResponseDto>> getMyListDibsPlaces() {
        List<GetMyListPlacesResponseDto> getPlaces = placeService.getMyListPlaces();
        return ResponseEntity.ok(getPlaces);
    }

    @PostMapping("/my-list")
    public ResponseEntity<Void> addMyListDibsPlace(KakaoPlaceInfo request) {
        placeService.triggerDibsMyListPlace(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/my-list/{kakaoPlaceId}")
    public ResponseEntity<Void> deleteMyListDibsPlace(@PathVariable Long kakaoPlaceId) {
        placeService.deleteMyListPlace(kakaoPlaceId);
        return ResponseEntity.ok().build();
    }
}
