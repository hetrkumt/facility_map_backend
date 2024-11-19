package com.example.demo.util.Authentication.loginAuthentication;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Authentication.AuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class LoginAuthenticationManager {
    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    @Autowired
    private BCryptPasswordEncoder encoder;
    public AuthenticationResult authenticateLocalPreAuthentication(Authentication preAuthentication) {
        String EmailOrSnsId = (String) preAuthentication.getPrincipal();
        String password = (String) preAuthentication.getCredentials();
        Optional<User> foundUserOptional  = userRepository.findByEmail(EmailOrSnsId);
        if (foundUserOptional.isPresent()) {
            User foundUser = foundUserOptional.get();
            System.out.println(password + " =?= "+ foundUser.getPassword());
            if (encoder.matches(password, foundUser.getPassword())) {
                // 인증이 성공했을 경우
                Authentication authentication = authenticationProvider.makeAuthenticationFrom(foundUser);
                authenticationProvider.setAuthenticationtoSecurityContextHolder(authentication);
                return AuthenticationResult.SUCCESS;
            } else {
                // 비밀번호가 일치하지 않는 경우
                System.out.println("Invalid password");
                return AuthenticationResult.INVALID_PASSWORD;
            }
        } else {
            // 사용자를 찾을 수 없는 경우
            System.out.println("User not found with email: " + EmailOrSnsId);
            return AuthenticationResult.USER_NOT_FOUND;
        }
    }

    public AuthenticationResult authenticateFormOAuthPreAuthentication(Authentication preAuthentication) {
        String EmailOrSnsId = (String) preAuthentication.getPrincipal();
        String password = (String) preAuthentication.getCredentials();
        Optional<User> foundUserOptional = userRepository.findBySnsId(EmailOrSnsId);
        if (foundUserOptional.isPresent()) {
            User foundUser = foundUserOptional.get();
            if (foundUser.getPassword().equals(password)) {
                // 인증이 성공했을 경우
                Authentication authentication = authenticationProvider.makeAuthenticationFrom(foundUser);
                authenticationProvider.setAuthenticationtoSecurityContextHolder(authentication);
                return AuthenticationResult.SUCCESS;
            } else {
                // 비밀번호가 일치하지 않는 경우
                System.out.println("Invalid password");
                return AuthenticationResult.INVALID_PASSWORD;
            }
        } else {
            // 사용자를 찾을 수 없는 경우
            System.out.println("User not found with SNS ID: " + EmailOrSnsId);
            return AuthenticationResult.USER_NOT_FOUND;
        }
    }

}
