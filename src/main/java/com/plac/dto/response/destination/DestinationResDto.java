package com.plac.dto.response.destination;

import com.plac.domain.destination.Destination;
import lombok.Getter;

@Getter
public class DestinationResDto {
    private final String id;
    private final String name;
    private final String thumbnailUrl;

    private DestinationResDto(Destination destination) {
        this.id = destination.getId();
        this.name = destination.getName();
        this.thumbnailUrl = destination.getThumbnailUrl();
    }

    public static DestinationResDto of(Destination destination) {
        return new DestinationResDto(destination);
    }
}
