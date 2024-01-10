package com.plac.domain.destination.controller;

import com.plac.domain.destination.dto.request.SearchDestinationRequest;
import com.plac.domain.destination.service.DestinationService;
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
@RequestMapping("/api/destination")
public class DestinationController {

    private final DestinationService destinationService;

    @PostMapping("/words")
    public ResponseEntity<?> searchDestinations(@RequestBody SearchDestinationRequest searchDestinationRequest){
        destinationService.createSearchWords(searchDestinationRequest);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }
}
