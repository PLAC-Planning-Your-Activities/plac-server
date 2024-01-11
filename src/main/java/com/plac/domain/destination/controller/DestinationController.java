package com.plac.domain.destination.controller;

import com.plac.domain.destination.dto.request.SearchDestinationRequest;
import com.plac.domain.destination.dto.response.PopularWordsResponse;
import com.plac.domain.destination.service.DestinationService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/destination")
public class DestinationController {

    private final DestinationService destinationService;

    @PostMapping("/words")
    public ResponseEntity<?> searchDestinations(@RequestBody SearchDestinationRequest searchDestinationRequest){
        System.out.println("== searchDestications() = ");
        destinationService.createSearchWords(searchDestinationRequest);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @GetMapping("/top6")
    public ResponseEntity<?> getTop6SearchWords(){
        List<PopularWordsResponse> result = destinationService.getTop6SearchWords();

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }
}
