package edu.hillel.user_rating_test_task.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.hillel.user_rating_test_task.entity.Role;
import edu.hillel.user_rating_test_task.entity.User;
import edu.hillel.user_rating_test_task.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserRegistrationService {

    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Set.of(Role.USER));
            userRepository.save(user);
        }
        return user;
    }
}
