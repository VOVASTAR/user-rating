package edu.hillel.user_rating_test_task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.hillel.user_rating_test_task.entity.UserRating;
import edu.hillel.user_rating_test_task.entity.dto.UserAverageRatingDto;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
    @Query("SELECT new edu.hillel.user_rating_test_task.entity.dto.UserAverageRatingDto(u, AVG(ur.starsRating)) " +
        "FROM UserRating ur JOIN ur.reviewedUser u " +
        "GROUP BY u")
    List<UserAverageRatingDto> findAverageStarsRatingForUsers();

    List<UserRating> findByReviewedUserId(Long reviewedUserId);

    Optional<UserRating> findByIdAndReviewedUserId(Long id, Long reviewedUserId);
}
