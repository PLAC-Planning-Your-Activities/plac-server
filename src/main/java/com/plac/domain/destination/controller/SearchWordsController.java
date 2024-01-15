package com.plac.domain.destination.controller;

import com.plac.domain.destination.dto.request.CreateSearchWordsRequest;
import com.plac.domain.destination.service.SearchWordsService;
import com.plac.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search-words")
public class SearchWordsController {

    private final SearchWordsService searchWordsService;

    @PostMapping("")
    public ResponseEntity<?> createSearchWords(@RequestBody CreateSearchWordsRequest userRequest){
        searchWordsService.createSearchWords(userRequest);

        return MessageUtil.buildResponseEntity(HttpStatus.OK, "success");
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getTop6SearchWords(@RequestParam("filter") int filter){
        List<String> result = searchWordsService.getTop6SearchWords(filter);

        return MessageUtil.buildResponseEntity(result, HttpStatus.OK, "success");
    }
}
