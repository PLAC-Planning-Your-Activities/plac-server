package com.plac.domain.place_review;

import com.plac.domain.AbstractTimeEntity;
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
@Table(name = "place_review_tag_mapping", indexes = {
        @Index(name = "idx_place_review_id", columnList = "place_review_id")
})
public class PlaceReviewTagMapping extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_review_id", nullable = false)
    private PlaceReview placeReview;

    @ManyToOne
    @JoinColumn(name = "place_review_tag_id", nullable = false)
    private PlaceReviewTag placeReviewTag;

}
