package edu.hillel.user_rating_test_task.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.hillel.user_rating_test_task.entity.Role;
import edu.hillel.user_rating_test_task.entity.User;
import edu.hillel.user_rating_test_task.repository.UserRepository;

@Configuration
public class DatabaseLoader {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            for (int i = 1; i <= 5; i++) {
                User user = User.builder()
                    .username("U" + i)
                    .password(passwordEncoder.encode(String.valueOf(i)))
                    .roles(Set.of(Role.USER))
                    .build();
                userRepository.save(user);
            }

            for (int i = 1; i <= 2; i++) {
                User user = User.builder()
                    .username("A" + i)
                    .password(passwordEncoder.encode(String.valueOf(i)))
                    .roles(Set.of(Role.ADMIN))
                    .build();
                userRepository.save(user);
            }
        };
    }

}
