package com.projectgym.repository;

import com.projectgym.model.RefreshToken;
import com.projectgym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    List<RefreshToken> findByUser(User user);

    void deleteById(Long token);

    User findUserByToken(String token);

    int findUserIdByToken(String token);
}
