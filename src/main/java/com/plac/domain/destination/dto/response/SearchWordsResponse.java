package com.plac.domain.destination.dto.response;

import lombok.Getter;

@Getter
public class SearchWordsResponse {
    private String name;

    public SearchWordsResponse(String name) {
        this.name = name;
    }
}
