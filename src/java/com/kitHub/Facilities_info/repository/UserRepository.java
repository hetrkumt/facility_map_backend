package com.kitHub.Facilities_info.repository;

import com.kitHub.Facilities_info.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(String userId);
    Optional<User> findBySnsId(String snsId);
    Optional<User> findByNickname(String email);
}

