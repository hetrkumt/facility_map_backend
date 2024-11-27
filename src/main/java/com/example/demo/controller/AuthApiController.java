package com.example.demo.controller;


import com.example.demo.domain.User;
import com.example.demo.dto.auth.AddLocalUserRequest;
import com.example.demo.dto.auth.AddOauthUserRequest;
import com.example.demo.dto.auth.LocalLoginRequest;
import com.example.demo.dto.auth.OAuthLoginRequest;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.LoginService;
import com.example.demo.service.ReviewService;
import com.example.demo.service.UserService;

import com.example.demo.util.Authentication.AuthenticationProvider;
import com.example.demo.util.Authentication.loginAuthentication.AuthenticationResult;
import com.example.demo.util.jwt.JwtProvider;
import com.example.demo.util.login.LoginSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private JwtProvider tokenProvider;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<String> checkEmail(@PathVariable String email) {
        boolean exists = userService.checkEmailExists(email);
        if (exists) {
            return ResponseEntity.status(409).body("이미 사용 중인 이메일입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        }
    }

    @GetMapping("/checknickname/{nickname}")
    public ResponseEntity<String> checkNickname(@PathVariable String nickname) {
        boolean exists = userService.checkNicknameExists(nickname);
        if (exists) {
            return ResponseEntity.status(409).body("이미 사용 중인 nickname 입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        }


    }

    @PostMapping("/signup/local")
    public ResponseEntity<?> registerLocalUser(@RequestBody AddLocalUserRequest user) {
        Long userResult = userService.saveLocal(user);
        if (userResult == null) {
            return ResponseEntity.badRequest().body("계정이 이미 존재 합니다");
        } else {
            return ResponseEntity.ok("회원 가입이 완료되었습니다");
        }
    }

    @PostMapping("/signup/oauth")
    public ResponseEntity<?> registerOAuthUser(@RequestBody AddOauthUserRequest user) {
        Long userResult = userService.saveOAuth(user);
        if (userResult == null) {
            return ResponseEntity.badRequest().body("계정이 이미 존재 합니다");
        } else {
            return ResponseEntity.ok("회원 가입이 완료되었습니다");
        }
    }

    @PostMapping("/local/login")
    public ResponseEntity<Map<String, String>> localLogin(@RequestBody LocalLoginRequest dto) {
        AuthenticationResult result = loginService.localLogin(dto);
        return handleAuthenticationResult(result);
    }

    @PostMapping("/oauth/login")
    public ResponseEntity<Map<String, String>> OAuthLogin(@RequestBody OAuthLoginRequest dto) {
        AuthenticationResult result = loginService.OAuthLogin(dto);
        return handleAuthenticationResult(result);
    }

    private ResponseEntity<Map<String, String>> handleAuthenticationResult(AuthenticationResult result) {
        HttpHeaders headers = new HttpHeaders();
        Map<String, String> tokens = new HashMap<>();
        switch (result) {
            case SUCCESS:
                tokens = loginSuccessHandler.makeTokensOnAuthenticationSuccess();
                String accessToken = tokens.get("accessToken");
                String refreshToken = tokens.get("refreshToken");

                headers.add("AccessToken", accessToken);
                headers.add("RefreshToken", refreshToken);

                // 디버그 로그 추가
                System.out.println("Access Token: " + accessToken);
                System.out.println("Refresh Token: " + refreshToken);
                System.out.println("Response Headers: " + headers);
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(tokens);

            case USER_NOT_FOUND:
                tokens.put("error", "User not found");
                return ResponseEntity.status(404).body(tokens);
            case INVALID_PASSWORD:
                tokens.put("error", "Invalid password");
                return ResponseEntity.status(401).body(tokens);
            default:
                tokens.put("error", "Authentication failed");
                return ResponseEntity.status(500).body(tokens);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationProvider.getAuthenticationFromSecurityContextHolder();
        if (authentication != null) {
            User user = authenticationProvider.getUserInfoFromSecurityContextHolder();

            // RefreshToken 삭제
            System.out.println("User ID: " + user.getId());
            refreshTokenRepository.deleteByUserId(user.getId());

            new SecurityContextLogoutHandler().logout(request, response, authentication);

            System.out.println("Logout successful for user ID: " + user.getId());
            return ResponseEntity.ok("Logout successful");
        }

        System.out.println("No user is currently logged in"); // 로그 추가
        return ResponseEntity.status(400).body("No user is currently logged in");
    }
}



