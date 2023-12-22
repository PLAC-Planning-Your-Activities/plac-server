package com.plac.controller;

import com.plac.dto.response.destination.DestinationResDto;
import com.plac.dto.response.destination.TopDestinationResDto;
import com.plac.service.destination.DestinationService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/destinations")
public class DestinationController {
    private final DestinationService destinationService;

    @GetMapping("")
    public ResponseEntity<?> getDestinations(
            @RequestParam(required = false) String query // TODO 이름 검색 기능 추가
    ) {
        List<DestinationResDto> responseData = destinationService.getDestinations();

        return MessageUtil.buildResponseEntity(responseData, HttpStatus.OK, "success");
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopDestinations(
            @RequestParam(name = "count", required = false, defaultValue = "7") @Min(1) @Max(30) int count
    ) {
        List<TopDestinationResDto> responseData = destinationService.getTopDestinations(count);

        return MessageUtil.buildResponseEntity(responseData, HttpStatus.OK, "success");
    }
}
