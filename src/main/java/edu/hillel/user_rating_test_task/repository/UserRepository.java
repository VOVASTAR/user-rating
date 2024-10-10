package edu.hillel.user_rating_test_task.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.hillel.user_rating_test_task.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);
}
