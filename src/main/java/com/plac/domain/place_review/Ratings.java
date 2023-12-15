package com.plac.domain.place_review;

import javax.persistence.Embeddable;

@Embeddable
public class Ratings {
    private Integer totalRating;
    private Integer flavorRating;
    private Integer priceRating;
    private Integer kindnessRating;
    private Integer cleannessRating;
    private Integer moodRating;
}
