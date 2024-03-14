package com.plac.domain.place.controller;

import com.plac.domain.place.dto.request.CreatePlacesRequest;
import com.plac.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
