package com.plac.domain.place.controller;

import com.plac.domain.place.dto.request.CreatePlaceRequest;
import com.plac.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<?> createPlaceInfo(@RequestBody CreatePlaceRequest placeRequest) throws Exception {
        placeService.createNewPlace(placeRequest);
        return ResponseEntity.ok().build();
    }
}
