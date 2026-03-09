package com.projectgym.service;

import com.projectgym.exception.InvalidTokenException;
import com.projectgym.exception.TokenExpiredException;
import com.projectgym.model.RefreshToken;
import com.projectgym.model.User;
import com.projectgym.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;

    private final RefreshTokenRepository refreshTokenRepository;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user, String deviceInfo){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION));
        refreshToken.setDeviceInfo(deviceInfo);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Refresh token nem található"));
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException("A refresh token lejárt. Jelentkezz be újra!");
        }
    }

    public List<RefreshToken> findAllByUser(User user){
        return refreshTokenRepository.findByUser(user);
    }

    @Transactional
    public void deleteByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }

    @Transactional
    public void deleteById(Integer token){
        refreshTokenRepository.deleteById((long) token);
    }

    public User getUserByToken(String refreshToken) {

        return refreshTokenRepository.findUserByToken(refreshToken);

    }

}
