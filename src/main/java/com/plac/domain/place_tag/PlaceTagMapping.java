package com.plac.domain.place_tag;

import com.plac.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "place_tag_mapping", indexes = {
        @Index(name = "idx_place_id", columnList = "place_id"),
        @Index(name = "idx_place_tag_id", columnList = "place_tag_id")
})
public class PlaceTagMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "place_tag_id", nullable = false)
    private PlaceTag placeTag;

}
