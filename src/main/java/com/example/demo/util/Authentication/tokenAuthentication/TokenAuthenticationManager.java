package com.example.demo.util.Authentication.tokenAuthentication;

import com.example.demo.domain.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;

import com.example.demo.repository.UserRepository;
import com.example.demo.util.jwt.JwtProperties;
import com.example.demo.util.jwt.JwtProvider;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TokenAuthenticationManager {
    private final JwtProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final UserRepository userRepository;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String ACCESS_TOKEN_PREFIX = "accesstoken ";


    public String getRefreshTokenFrom(String accessToken) {
        Long userId = tokenProvider.getUserId(accessToken);
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(userId);
        if (!refreshToken.isPresent()) {
            return null;
        }
        else {
            return refreshToken.get().getRefreshToken();
        }
    }

    public Claims validateAndParseToken(String token) throws TokenValidationException {
        // 토큰을 검증합니다.
        TokenValidationResult result = validateToken(token);
        if (result != TokenValidationResult.VALID) {
            throw new TokenValidationException(result, "Token validation failed with reason: " + result);
        }
        // 검증 성공 시 파싱된 클레임 객체를 반환합니다.
        return getParsedToken(token);
    }


    public TokenValidationResult validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return TokenValidationResult.MISSING;
        }

        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return TokenValidationResult.VALID;
        } catch (ExpiredJwtException e) {
            return TokenValidationResult.EXPIRED;
        } catch (SignatureException e) {
            return TokenValidationResult.SIGNATURE_INVALID;
        } catch (MalformedJwtException e) {
            return TokenValidationResult.MALFORMED;
        } catch (UnsupportedJwtException e) {
            return TokenValidationResult.UNSUPPORTED;
        } catch (Exception e) {
            return TokenValidationResult.INVALID;
        }
    }

    public Claims getParsedToken(String token) throws TokenValidationException {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenValidationException(TokenValidationResult.EXPIRED, "Token has expired");
        } catch (SignatureException e) {
            throw new TokenValidationException(TokenValidationResult.SIGNATURE_INVALID, "Token signature is invalid");
        } catch (MalformedJwtException e) {
            throw new TokenValidationException(TokenValidationResult.MALFORMED, "Token is malformed");
        } catch (UnsupportedJwtException e) {
            throw new TokenValidationException(TokenValidationResult.UNSUPPORTED, "Token is unsupported");
        } catch (Exception e) {
            throw new TokenValidationException(TokenValidationResult.INVALID, "Token is invalid");
        }
    }


    public void clearAuthenticate() { SecurityContextHolder.clearContext(); }
}
