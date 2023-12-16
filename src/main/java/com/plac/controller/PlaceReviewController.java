package com.plac.controller;

import com.plac.dto.request.place_review.PlaceReviewReqDto;
import com.plac.service.place_review.PlaceReviewService;
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
@RequestMapping("/api/place-review")
public class PlaceReviewController {

    private final PlaceReviewService placeReviewService;

    @PostMapping("")
    public ResponseEntity<?> writePlaceReview(@RequestBody PlaceReviewReqDto req){
        placeReviewService.writeReview(req);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }
}
