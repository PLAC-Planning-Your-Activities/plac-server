package com.plac.domain.destination.controller;

import com.plac.domain.destination.service.DestinationService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/destination")
public class DestinationController {

    private final DestinationService destinationService;

    @GetMapping("/popular")
    public ResponseEntity<?> getTop7Destinations(@RequestParam("filter") int filter) {
        List<String> result = destinationService.getTop7SearchWords(filter);

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }

}
