package com.plac.domain.destination.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateSearchWordsRequest {
    private List<String> words;

    @Builder
    public CreateSearchWordsRequest(List<String> words) {
        this.words = words;
    }
}
