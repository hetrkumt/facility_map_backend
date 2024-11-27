package com.example.demo.repository;

import com.example.demo.domain.User;
import com.example.demo.domain.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(String userId);

    Optional<User> findBySnsId(String snsId);

    Optional<User> findByNickname(String nickname);

}
