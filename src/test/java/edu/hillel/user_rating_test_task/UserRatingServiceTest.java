package edu.hillel.user_rating_test_task;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import edu.hillel.user_rating_test_task.entity.Role;
import edu.hillel.user_rating_test_task.entity.User;
import edu.hillel.user_rating_test_task.entity.UserRating;
import edu.hillel.user_rating_test_task.entity.dto.UserAverageRatingDto;
import edu.hillel.user_rating_test_task.entity.dto.UserRatingDto;
import edu.hillel.user_rating_test_task.repository.UserRatingRepository;
import edu.hillel.user_rating_test_task.service.UserRatingService;

@ExtendWith(MockitoExtension.class)
public class UserRatingServiceTest {

    @Mock
    private UserRatingRepository userRatingRepository;
    @InjectMocks
    private UserRatingService userRatingService;

    @Test
    void testGetAllUsersWithAverageRating() {
        User user = User.builder()
            .id(2L)
            .rating(4.99)
            .build();
        List<UserAverageRatingDto> mockUserAverageRatingDto = List.of(new UserAverageRatingDto(user, 4.99));
        when(userRatingRepository.findAverageStarsRatingForUsers()).thenReturn(mockUserAverageRatingDto);

        List<UserAverageRatingDto> allUsersWithAverageRating = userRatingService.getAllUsersWithAverageRating();

        Assertions.assertNotNull(allUsersWithAverageRating);
        Assertions.assertEquals(1, allUsersWithAverageRating.size());
        Assertions.assertEquals(2L, allUsersWithAverageRating.getFirst().getReviewedUser().getId());
        Assertions.assertEquals(4.99, allUsersWithAverageRating.getFirst().getReviewedUser().getRating());
        verify(userRatingRepository).findAverageStarsRatingForUsers();
    }

    @Test
    void testSetRatingToUser() {
        User reviewedUser = new User(1L, "reviewedUser", Set.of(Role.ADMIN));
        User reviewer = new User(2L, "reviewer", Set.of(Role.USER));
        String comment = "comment";
        Byte starsRating = 4;
        UserRating mockUserRating = UserRating.builder()
            .reviewedUser(reviewedUser)
            .reviewer(reviewer)
            .comment(comment)
            .starsRating(starsRating)
            .build();

        when(userRatingRepository.save(any(UserRating.class))).thenReturn(mockUserRating);

        UserRatingDto userRating = userRatingService.setRatingToUser(reviewedUser, reviewer, comment, starsRating);

        Assertions.assertNotNull(userRating);
        Assertions.assertEquals(userRating.getReviewedUserId(), reviewedUser.getId());
        Assertions.assertEquals(userRating.getReviewerId(), reviewer.getId());
        Assertions.assertEquals(userRating.getComment(), comment);
        Assertions.assertEquals(userRating.getStarsRating(), starsRating);
        verify(userRatingRepository).save(any(UserRating.class));
    }

    @Test
    void testSetRatingToUserSelfReview() {
        User user = new User(1L, "User", Set.of(Role.ADMIN));
        String comment = "Self review";
        Byte stars = 5;
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            userRatingService.setRatingToUser(user, user, comment, stars));
        Assertions.assertEquals(exception.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(exception.getReason(), "You can not review yourself");
    }


    @Test
    void testDeleteUserRating() {
        Long userRatingId = 1L;
        User reviewedUser = new User(1L, "reviewedUser", Set.of(Role.ADMIN));
        User reviewer = new User(2L, "reviewer", Set.of(Role.USER));
        UserRating mockUserRating = UserRating.builder()
            .id(userRatingId)
            .reviewedUser(reviewedUser)
            .reviewedUser(reviewer)
            .build();

        when(userRatingRepository.findByIdAndReviewedUserId(userRatingId, reviewedUser.getId())).thenReturn(Optional.of(mockUserRating));
        userRatingService.deleteUserRating(reviewedUser.getId(), userRatingId);
        verify(userRatingRepository).findByIdAndReviewedUserId(userRatingId, reviewedUser.getId());
    }

    @Test
    void testDeleteUserRatingNoExistingUser() {
        Long userRatingId = 10L;
        User reviewedUser = new User(1L, "reviewedUser", Set.of(Role.ADMIN));
        when(userRatingRepository.findByIdAndReviewedUserId(userRatingId, reviewedUser.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userRatingService.deleteUserRating(reviewedUser.getId(), userRatingId));
        verify(userRatingRepository).findByIdAndReviewedUserId(userRatingId, reviewedUser.getId());
    }

    @Test
    void starsRatingShouldBeMoreOrEqualsOne() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        UserRating userRating = UserRating.builder()
            .reviewer(new User(2L, "reviewer", Set.of(Role.USER)))
            .reviewedUser(new User(1L, "reviewedUser", Set.of(Role.ADMIN)))
            .comment("comment")
            .starsRating((byte) 0)
            .build();

        Set<ConstraintViolation<UserRating>> violations = validator.validate(userRating);

        Assertions.assertNotNull(validator);
        Assertions.assertEquals(violations.iterator().next().getMessage(), "must be greater than or equal to 1");
    }

    @Test
    void starsRatingShouldBeLessOrEqualsFive() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        UserRating userRating = UserRating.builder()
            .reviewer(new User(2L, "reviewer", Set.of(Role.USER)))
            .reviewedUser(new User(1L, "reviewedUser", Set.of(Role.ADMIN)))
            .comment("comment")
            .starsRating((byte) 6)
            .build();

        Set<ConstraintViolation<UserRating>> violations = validator.validate(userRating);

        Assertions.assertNotNull(validator);
        Assertions.assertEquals(violations.iterator().next().getMessage(), "must be less than or equal to 5");
    }
}
