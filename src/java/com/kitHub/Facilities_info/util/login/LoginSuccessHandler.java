package com.kitHub.Facilities_info.util.login;

import com.kitHub.Facilities_info.domain.RefreshToken;
import com.kitHub.Facilities_info.domain.User;
import com.kitHub.Facilities_info.repository.RefreshTokenRepository;
import com.kitHub.Facilities_info.repository.UserRepository;

import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import com.kitHub.Facilities_info.util.jwt.JwtProvider;
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
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }
}