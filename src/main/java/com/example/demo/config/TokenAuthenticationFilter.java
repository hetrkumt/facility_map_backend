package com.example.demo.config;

import com.example.demo.util.Authentication.AuthenticationProvider;

import com.example.demo.util.Authentication.tokenAuthentication.TokenAuthenticationManager;
import com.example.demo.util.Authentication.tokenAuthentication.TokenValidationResult;
import com.example.demo.util.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider tokenProvider;
    private final TokenAuthenticationManager tokenAuthenticationManager;
    private final AuthenticationProvider authenticationProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String ACCESS_TOKEN_PREFIX = "accesstoken ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);

        if (token != null) {
            String refreshToken = tokenAuthenticationManager.getRefreshTokenFrom(token);
            TokenValidationResult tokenValidationResult = tokenAuthenticationManager.validateToken(refreshToken);

            if (tokenValidationResult == TokenValidationResult.VALID) {
                Authentication authentication = tokenProvider.getAuthentication(refreshToken);
                authenticationProvider.setAuthenticationtoSecurityContextHolder(authentication);
            } else {
                handleTokenValidationFailure(tokenValidationResult, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleTokenValidationFailure(TokenValidationResult tokenValidationResult, HttpServletResponse response) throws IOException {
        switch (tokenValidationResult) {
            case MISSING:
            case INVALID:
            case MALFORMED:
            case UNSUPPORTED:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid token");
                break;
            case EXPIRED:
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                break;
            case SIGNATURE_INVALID:
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature");
                break;
            default:
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
                break;
        }
    }
    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(ACCESS_TOKEN_PREFIX)) {
            return authorizationHeader.substring(ACCESS_TOKEN_PREFIX.length());
        }

        return null;
    }


}
