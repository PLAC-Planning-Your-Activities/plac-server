package com.plac.controller;

import com.plac.dto.request.place_review.PlaceReviewRateReqDto;
import com.plac.dto.request.place_review.PlaceReviewReqDto;
import com.plac.dto.response.place_review.PlaceReviewResDto;
import com.plac.service.place_review.PlaceReviewService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/like")
    public ResponseEntity<?> addLikeToPlaceReview(@RequestBody PlaceReviewRateReqDto req){
        placeReviewService.addLikeToPlaceReview(req);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @PostMapping("/disLike")
    public ResponseEntity<?> addiDisLikeToPlaceReview(@RequestBody PlaceReviewRateReqDto req){
        placeReviewService.addDisLikeToPlaceReview(req);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @GetMapping("/paging")
    public ResponseEntity<?> searchPlaceReviews(@RequestParam("placeId") Long placeId,
                                             @RequestParam("sortBy") String sortBy,
                                             @RequestParam("page") int page) {

        List<PlaceReviewResDto> placeReviews = placeReviewService.getPlaceReviews(placeId, sortBy, page);

        return MessageUtil.buildResponseEntity(placeReviews, HttpStatus.OK, "success");
    }

}
