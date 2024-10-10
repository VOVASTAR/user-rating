package edu.hillel.user_rating_test_task.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.hillel.user_rating_test_task.entity.User;
import edu.hillel.user_rating_test_task.entity.dto.UserAverageRatingDto;
import edu.hillel.user_rating_test_task.entity.dto.UserRatingDto;
import edu.hillel.user_rating_test_task.service.UserRatingService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/user-rating")
@Validated
public class UserRatingController {

    private final UserRatingService userRatingService;

    @GetMapping("/all-users-rating")
    public List<UserAverageRatingDto> getAllUsersWithAverageRating() {
        return userRatingService.getAllUsersWithAverageRating();
    }

    record UserRatingParams(String comment, @Min(1) @Max(5) Byte starsRating) {
    }

    @PostMapping("/set-rating/{id}")
    public UserRatingDto setRatingToUser(@PathVariable("id") User reviewedUser, @AuthenticationPrincipal User user,
                                         @RequestBody @Valid UserRatingParams userRatingParams) {
        String comment = userRatingParams.comment();
        Byte stars = userRatingParams.starsRating;
        return userRatingService.setRatingToUser(reviewedUser, user, comment, stars);
    }

    @GetMapping("/{userId}")
    public List<UserRatingDto> getUserRating(@PathVariable("userId") Long userId) {
        return userRatingService.getUserAllRating(userId);
    }


    @DeleteMapping("/{userId}/{ratingId}")
    @PreAuthorize("hasAuthority(T(edu.hillel.user_rating_test_task.entity.Role).ADMIN)")
    public void deleteUserRating(@PathVariable("userId") Long userId, @PathVariable("ratingId") Long userRatingId) {
        userRatingService.deleteUserRating(userId, userRatingId);
    }
}
