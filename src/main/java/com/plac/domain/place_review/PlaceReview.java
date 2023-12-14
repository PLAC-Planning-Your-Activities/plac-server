package com.plac.domain.place_review;

import com.plac.domain.AbstractTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "place_review")
public class PlaceReview extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(name = "total_rating")
    private Integer totalRating;

    @Column(name = "flavor_rating")
    private Integer flavorRating;

    @Column(name = "price_rating")
    private Integer priceRating;

    @Column(name = "kindness_rating")
    private Integer kindnessRating;

    @Column(name = "cleanness_rating")
    private Integer cleannessRating;

    @Column(name = "mood_rating")
    private Integer moodRating;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "placeReview")
    private Set<PlaceReviewTagMapping> placeReviewTagMappings = new HashSet<>();

    @OneToMany(mappedBy = "placeReview")
    private Set<PlaceReviewImage> placeReviewImages = new HashSet<>();

    @OneToMany(mappedBy = "placeReview")
    private Set<PlaceReviewLike> placeReviewLikes = new HashSet<>();

    @OneToMany(mappedBy = "placeReview")
    private Set<PlaceReviewDislike> placeReviewDislikes = new HashSet<>();

}
