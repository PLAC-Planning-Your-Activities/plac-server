package com.plac.controller;

import com.plac.dto.request.place.PlaceReqDto;
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
    public ResponseEntity<?> searchPlaceSummaryInfo(@RequestBody PlaceReqDto req) throws Exception {
        List<PlaceResDto> placeSummaryInfo = placeService.getPlaceSummaryInfo(req);

        return MessageUtil.buildResponseEntity(placeSummaryInfo, HttpStatus.OK, "success");
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getPlaceDetails(@RequestBody PlaceReqDto req) throws Exception {
        List<PlaceResDto> placeSummaryInfo = placeService.getPlaceSummaryInfo(req);

        return MessageUtil.buildResponseEntity(placeSummaryInfo, HttpStatus.OK, "success");
    }
}
