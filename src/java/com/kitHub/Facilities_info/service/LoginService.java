package com.kitHub.Facilities_info.service;

import com.kitHub.Facilities_info.domain.User;
import com.kitHub.Facilities_info.dto.auth.LocalLoginRequest;
import com.kitHub.Facilities_info.dto.auth.OAuthLoginRequest;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import com.kitHub.Facilities_info.util.Authentication.loginAuthentication.LoginAuthenticationManager;
import com.kitHub.Facilities_info.util.Authentication.loginAuthentication.AuthenticationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    @Autowired
    private final AuthenticationProvider authenticationProvider;
    @Autowired
    private final LoginAuthenticationManager loginAuthenticationManager;
    @Autowired
    private BCryptPasswordEncoder encoder;
    public AuthenticationResult localLogin(LocalLoginRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User PreAuthuser =  User.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .provider("Local")
                .build();
        Authentication preAuthentication = authenticationProvider.makePreAuthenticationFrom(PreAuthuser);
        return loginAuthenticationManager.authenticateLocalPreAuthentication(preAuthentication);
    }

    public AuthenticationResult OAuthLogin(OAuthLoginRequest dto) {
        User PreAuthuser =  User.builder()
                .snsId(dto.getSnsId())
                .provider("OAuth")
                .build();
        Authentication preAuthentication = authenticationProvider.makePreAuthenticationFrom(PreAuthuser);
        return loginAuthenticationManager.authenticateFormOAuthPreAuthentication(preAuthentication);
    }
}
