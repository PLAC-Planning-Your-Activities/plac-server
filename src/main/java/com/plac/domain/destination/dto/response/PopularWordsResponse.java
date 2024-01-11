package com.plac.domain.destination.dto.response;

import lombok.Getter;

@Getter
public class PopularWordsResponse {
    private String name;

    public PopularWordsResponse(String name) {
        this.name = name;
    }
}
