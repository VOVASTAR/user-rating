package edu.hillel.user_rating_test_task.entity.dto;

import edu.hillel.user_rating_test_task.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAverageRatingDto {
    private User reviewedUser;
    private Double averageStarsRating;
}
