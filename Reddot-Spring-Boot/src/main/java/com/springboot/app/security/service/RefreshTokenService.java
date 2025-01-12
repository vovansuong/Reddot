package com.springboot.app.security.service;

import com.springboot.app.accounts.entity.User;
import com.springboot.app.accounts.repository.UserRepository;
import com.springboot.app.dto.response.AckCodeType;
import com.springboot.app.dto.response.ServiceResponse;
import com.springboot.app.security.entity.RefreshToken;
import com.springboot.app.security.exception.ResourceNotFoundException;
import com.springboot.app.security.exception.TokenRefreshException;
import com.springboot.app.security.repository.RefreshTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final int MAX_TOKENS = 3;
    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);
    @Value("${springboot.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if (user != null) {
            return generateRefreshTokenByUser(user);
        }
        return null;
    }

    public RefreshToken createRefreshTokenByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        if (user != null) {
            return generateRefreshTokenByUser(user);
        }
        return null;
    }

    private RefreshToken generateRefreshTokenByUser(User user) {
        List<RefreshToken> userTokens = refreshTokenRepository.findByUser(user);

        if (userTokens.size() >= MAX_TOKENS) {
            refreshTokenRepository.delete(userTokens.getFirst());  // delete the oldest token
        }
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setAvailable(true);

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        if (!token.isAvailable()) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was used. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        refreshTokenRepository.deleteByUser(user);
    }

    @Transactional
    public ServiceResponse<Void> deleteByToken(String token, Long userId) {
        ServiceResponse<Void> response = new ServiceResponse<>();
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElse(null);
        if (refreshToken == null) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Token not found");
            return response;
        }
        if (!Objects.equals(refreshToken.getUser().getId(), userId)) {
            response.setAckCode(AckCodeType.FAILURE);
            response.addMessage("Token not found for this user");
            return response;
        }
        refreshTokenRepository.delete(refreshToken);
        return response;
    }

}
