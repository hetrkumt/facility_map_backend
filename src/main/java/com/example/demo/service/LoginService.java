package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.dto.auth.LocalLoginRequest;
import com.example.demo.dto.auth.OAuthLoginRequest;
import com.example.demo.util.Authentication.AuthenticationProvider;
import com.example.demo.util.Authentication.loginAuthentication.LoginAuthenticationManager;
import com.example.demo.util.Authentication.loginAuthentication.AuthenticationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final AuthenticationProvider authenticationProvider;
    private final LoginAuthenticationManager loginAuthenticationManager;

    public AuthenticationResult localLogin(LocalLoginRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User PreAuthuser =  User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .provider(dto.getProvider())
                .build();
        Authentication preAuthentication = authenticationProvider.makePreAuthenticationFrom(PreAuthuser);
        return loginAuthenticationManager.authenticateLocalPreAuthentication(preAuthentication);
    }

    public AuthenticationResult OAuthLogin(OAuthLoginRequest dto) {
        User PreAuthuser =  User.builder()
                .snsId(dto.getSnsId())
                .provider(dto.getProvider())
                .nickname(dto.getNickname())
                .build();
        Authentication preAuthentication = authenticationProvider.makePreAuthenticationFrom(PreAuthuser);
        return loginAuthenticationManager.authenticateFormOAuthPreAuthentication(preAuthentication);
    }
}
