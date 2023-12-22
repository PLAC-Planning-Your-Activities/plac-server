package com.plac.domain.destination;

import com.plac.domain.AbstractBaseDocument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Document
public class Destination extends AbstractBaseDocument {
    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String thumbnailUrl;
}
