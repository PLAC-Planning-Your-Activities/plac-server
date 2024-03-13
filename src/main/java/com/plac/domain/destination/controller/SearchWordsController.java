package com.plac.domain.destination.controller;

import com.plac.domain.destination.dto.request.CreateSearchWordsRequest;
import com.plac.domain.destination.service.SearchWordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search-words")
public class SearchWordsController {

    private final SearchWordsService searchWordsService;

    @PostMapping
    public ResponseEntity<Void> createSearchWords(@RequestBody CreateSearchWordsRequest userRequest){
        searchWordsService.createSearchWords(userRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getTop6SearchWords(@RequestParam("filter") int filter){
        List<String> result = searchWordsService.getTop6SearchWords(filter);
        return ResponseEntity.ok().body(result);
    }
}
