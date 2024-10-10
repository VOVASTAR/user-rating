package edu.hillel.user_rating_test_task.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import edu.hillel.user_rating_test_task.entity.User;
import edu.hillel.user_rating_test_task.entity.UserRating;
import edu.hillel.user_rating_test_task.entity.dto.UserAverageRatingDto;
import edu.hillel.user_rating_test_task.entity.dto.UserRatingDto;
import edu.hillel.user_rating_test_task.repository.UserRatingRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRatingService {

    private final UserRatingRepository userRatingRepository;

    public List<UserAverageRatingDto> getAllUsersWithAverageRating() {
        return userRatingRepository.findAverageStarsRatingForUsers();
    }


    public UserRatingDto setRatingToUser(User reviewedUser, User user, String comment, Byte stars) {
        if (user.getId().equals(reviewedUser.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not review yourself");
        }
        UserRating assessmentUser = UserRating.builder()
            .reviewer(user)
            .reviewedUser(reviewedUser)
            .comment(comment)
            .starsRating(stars)
            .build();
        userRatingRepository.save(assessmentUser);
        return new UserRatingDto(assessmentUser);
    }

    public List<UserRatingDto> getUserAllRating(Long reviewedUserId) {
        List<UserRating> getUserRating = userRatingRepository.findByReviewedUserId(reviewedUserId);
        return getUserRating.stream()
            .map(UserRatingDto::new)
            .toList();
    }

    @Transactional
    public void deleteUserRating(Long reviewedUserId, Long userRatingId) {
        Optional<UserRating> existRating = userRatingRepository.findByIdAndReviewedUserId(userRatingId, reviewedUserId);
        if (existRating.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no such rating in this user");
        }
        userRatingRepository.deleteById(userRatingId);
    }
}
