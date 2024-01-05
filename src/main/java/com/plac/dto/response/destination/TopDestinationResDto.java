package com.plac.dto.response.destination;

import lombok.Getter;

@Getter
public class TopDestinationResDto {
    private final int ranking;
    private final String destinationId;
    private final String name;
    private final String thumbnailUrl;

    private TopDestinationResDto(int ranking, String destinationId, String name, String thumbnailUrl) {
        this.ranking = ranking;
        this.destinationId = destinationId;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
    }

    public static TopDestinationResDto of(int ranking, String destinationId, String name, String thumbnailUrl) {
        return new TopDestinationResDto(ranking, destinationId, name, thumbnailUrl);
    }
}
