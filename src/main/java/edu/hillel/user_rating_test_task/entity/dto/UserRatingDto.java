package edu.hillel.user_rating_test_task.entity.dto;

import edu.hillel.user_rating_test_task.entity.UserRating;
import lombok.Data;

@Data
public class UserRatingDto {

    private Long id;
    private Long reviewerId;
    private Long reviewedUserId;
    private String comment;
    private Byte starsRating;

    public UserRatingDto(UserRating userRating) {
        this.id = userRating.getId();
        this.reviewerId = userRating.getReviewer().getId();
        this.reviewedUserId = userRating.getReviewedUser().getId();
        this.comment = userRating.getComment();
        this.starsRating = userRating.getStarsRating();
    }
}
