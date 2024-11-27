package com.example.demo.util.login;

import com.example.demo.domain.RefreshToken;
import com.example.demo.domain.User;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.util.Authentication.AuthenticationProvider;
import com.example.demo.util.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    private final AuthenticationProvider authenticationProvider;
    private final JwtProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public Map<String, String> makeTokensOnAuthenticationSuccess() {
        Authentication authentication = authenticationProvider.getAuthenticationFromSecurityContextHolder();
        User user = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                user = (User) principal;
                System.out.println("Principal is an instance of User. User email: " + user.getEmail());
                // User 타입에 대한 처리 로직 추가
            } else {
                OAuth2User oAuth2User = (OAuth2User) principal;
                user = userRepository.findBySnsId((String) oAuth2User.getAttributes().get("SnsId")).get();
                System.out.println("Principal is an instance of OAuth2User. Attributes: " + oAuth2User.getAttributes());
                // OAuth2User 타입에 대한 처리 로직 추가
            }
        }

        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
         saveRefreshToken(user.getId(), refreshToken);

        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> {
                    System.out.println("Existing refresh token found for user ID: " + userId);
                    return entity.update(newRefreshToken);
                })
                .orElseGet(() -> {
                    System.out.println("No existing refresh token found, creating new one for user ID: " + userId);
                    return new RefreshToken(userId, newRefreshToken);
                });

        refreshTokenRepository.save(refreshToken);
        System.out.println("Refresh token for user ID " + userId + " has been saved: " + newRefreshToken);
    }

}