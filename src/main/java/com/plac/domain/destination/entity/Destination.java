package com.plac.domain.destination.entity;

import com.plac.common.AbstractTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Destination extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter
    private String name;

    @OneToMany(mappedBy = "destination")
    List<DestinationMapping> destinationMappings = new ArrayList<>();

    @Builder
    public Destination(String name) {
        this.name = name;
    }
}
