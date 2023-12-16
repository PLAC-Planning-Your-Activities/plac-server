package com.plac.dto.response.place_review;

import com.plac.domain.User;
import com.plac.domain.place_review.PlaceReview;
import com.plac.domain.place_review.Ratings;
import com.plac.dto.response.user.UserResDto;
import com.plac.service.user.UserService;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceReviewResDto {
    private Long id;
    private Long userId;
    private String userProfileName;
    private LocalDateTime createdAt;
    private Ratings ratings;
    private String content;
    private int like;
    private int dislike;
    private boolean myReview;
    private boolean pickLike;
    private boolean pickDisLike;
}
