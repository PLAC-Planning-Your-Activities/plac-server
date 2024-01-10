package com.plac.domain.destination;

import com.plac.common.AbstractTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Destination extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long external_id;

    private String imageUrl;

    private int count;

    @Builder
    public Destination(Long id, Long external_id, String imageUrl, int count) {
        this.external_id = external_id;
        this.imageUrl = imageUrl;
        this.count = count;
    }
}
