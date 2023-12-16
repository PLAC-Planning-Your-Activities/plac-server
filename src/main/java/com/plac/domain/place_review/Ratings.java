package com.plac.domain.place_review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Ratings {
    private double totalRating;
    private int flavorRating;
    private int priceRating;
    private int kindnessRating;
    private int cleannessRating;
    private int moodRating;
}
