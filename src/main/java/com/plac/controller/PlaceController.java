package com.plac.controller;

import com.plac.dto.request.place.PlaceDetailsReqDto;
import com.plac.dto.request.place.PlaceReqDto;
import com.plac.dto.response.place.PlaceDetailsResDto;
import com.plac.dto.response.place.PlaceResDto;
import com.plac.service.place.PlaceService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping("")
    public ResponseEntity<?> searchSummaryInfo(@RequestBody PlaceReqDto req) throws Exception {
        List<PlaceResDto> placeSummaryInfo = placeService.getPlacesSummaryInfo(req);

        return MessageUtil.buildResponseEntity(placeSummaryInfo, HttpStatus.OK, "success");
    }

    @GetMapping("/detail")
    public ResponseEntity<?> searchPlaceDetails( @RequestParam("placeId") Long placeId) throws Exception {
        PlaceDetailsResDto placeDetails = placeService.getPlaceDetails(placeId);

        return MessageUtil.buildResponseEntity(placeDetails, HttpStatus.OK, "success");
    }

}
