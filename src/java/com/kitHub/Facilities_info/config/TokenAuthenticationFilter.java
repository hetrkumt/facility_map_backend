package com.kitHub.Facilities_info.config;

import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;

import com.kitHub.Facilities_info.util.Authentication.tokenAuthentication.TokenAuthenticationManager;
import com.kitHub.Facilities_info.util.Authentication.tokenAuthentication.TokenValidationResult;
import com.kitHub.Facilities_info.util.jwt.JwtProvider;
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
    private final static String ACCESS_TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 요청 정보 출력
        logRequestDetails(request);

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        System.out.println("authorizationHeader=" + authorizationHeader);
        String token = getAccessToken(authorizationHeader);
        System.out.println("Access token=" + token);

        if (token != null) {
            System.out.println("token=" + token);
            TokenValidationResult tokenValidationResult = tokenAuthenticationManager.validateToken(token);

            if (tokenValidationResult == TokenValidationResult.VALID) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                authenticationProvider.setAuthenticationtoSecurityContextHolder(authentication);
            } else {
                handleTokenValidationFailure(tokenValidationResult, response);
                System.out.println(tokenValidationResult);
                return;
            }
        }
        else {
            System.out.println("token MISSING");

//            handleTokenValidationFailure(TokenValidationResult.MISSING, response);
//            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleTokenValidationFailure(TokenValidationResult tokenValidationResult, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        switch (tokenValidationResult) {
            case MISSING:
                //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Token is missing\"}");
                //break;
            case INVALID:
                //response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Invalid token\"}");
                //break;
            case MALFORMED:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Malformed token\"}");
                break;
            case UNSUPPORTED:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "{\"error\": \"Unsupported token\"}");
                break;
            case EXPIRED:
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "{\"error\": \"Token expired\"}");
                break;
            case SIGNATURE_INVALID:
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "{\"error\": \"Invalid token signature\"}");
                break;
            default:
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "{\"error\": \"An unexpected error occurred\"}");
                break;
        }
    }



    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(ACCESS_TOKEN_PREFIX)) {
            return authorizationHeader.substring(ACCESS_TOKEN_PREFIX.length());
        }

        return null;
    }

    private void logRequestDetails(HttpServletRequest request) {
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Headers:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                System.out.println(headerName + ": " + request.getHeader(headerName))
        );
        // 필요한 경우, 추가 정보를 출력할 수 있습니다.
    }


}

