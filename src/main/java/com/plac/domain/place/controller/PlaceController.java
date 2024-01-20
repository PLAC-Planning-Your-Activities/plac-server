package com.plac.domain.place.controller;

import com.plac.domain.place.dto.request.CreatePlaceLikeRequest;
import com.plac.domain.place.dto.request.CreatePlaceRequest;
import com.plac.domain.place.dto.response.CreatePlaceLikeResponse;
import com.plac.domain.place.service.PlaceLikeService;
import com.plac.domain.place.service.PlaceService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final PlaceLikeService placeLikeService;

    @PostMapping("")
    public ResponseEntity<?> createPlaceInfo(@RequestBody CreatePlaceRequest placeRequest) throws Exception {
        placeService.createNewPlace(placeRequest);
        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @PostMapping("/like")
    public ResponseEntity<?> createPlaceLike(
            @RequestBody CreatePlaceLikeRequest placeLikeRequest
    ){
        Long placeLikeId = placeLikeService.createPlaceLike(placeLikeRequest);
        CreatePlaceLikeResponse result = new CreatePlaceLikeResponse(placeLikeId);

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

}
