package edu.hillel.user_rating_test_task.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.hillel.user_rating_test_task.entity.User;
import edu.hillel.user_rating_test_task.service.UserRegistrationService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/registration")
public class RegistrationController {

    public final UserRegistrationService userRegistrationService;

    @PostMapping
    public User registerUser(@RequestBody User user) {
        userRegistrationService.registerUser(user);
        return user;
    }

}
