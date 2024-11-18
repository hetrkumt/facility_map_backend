package com.example.demo.util.jwt;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.Authentication.AuthenticationProvider;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private final UserRepository userRepository;

    private final AuthenticationProvider authenticationProvider;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        String snsIdOrEmail;
        if (user.getProvider().equals("local")) {
            snsIdOrEmail = user.getEmail();
        }
        else {
            snsIdOrEmail = user.getSnsId();
        }
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("snsIdOrEmail",snsIdOrEmail)
                .claim("provider",user.getProvider())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public String regenerateToken(String token) {
        Long userId = getUserId(token);
        Optional <User> userOpt = userRepository.findById(userId);
        User user = userOpt.get();
        return generateToken(user, Duration.ofHours(1));
    }




    public Authentication getAuthentication(String token) {
        Long userId = getUserId(token);
        Optional <User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return authenticationProvider.makeAuthenticationFrom(user);
        }
        else{
            return null;
        }
    }


    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("id", Long.class);
    }



    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
