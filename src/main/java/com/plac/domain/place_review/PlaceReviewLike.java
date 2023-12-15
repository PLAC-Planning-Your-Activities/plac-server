package com.plac.domain.place_review;

import com.plac.domain.AbstractTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "place_review_like", indexes = {
        @Index(name = "idx_place_review_user", columnList = "place_review_id, user_id")
})
public class PlaceReviewLike extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_review_id", nullable = false)
    private PlaceReview placeReview;

    @Column(name = "user_id", nullable = false)
    private Long userId;

}

