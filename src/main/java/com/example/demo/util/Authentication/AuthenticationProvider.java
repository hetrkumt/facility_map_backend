package com.example.demo.util.Authentication;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthenticationProvider {
    @Autowired
    private UserRepository userRepository;

    public Authentication getAuthenticationFromSecurityContextHolder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication;
        }
        else{
            return null;
        }
    }

    public void setAuthenticationtoSecurityContextHolder(Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    public Authentication makePreAuthenticationFrom(User user){
        String userEmailOrSnsId = user.getEmail();
        if (user.getEmail() != null) {
            userEmailOrSnsId = user.getEmail();
            Authentication preAuth = new UsernamePasswordAuthenticationToken(
                    userEmailOrSnsId,
                    user.getPassword());
            return preAuth;
        }
        else {
            userEmailOrSnsId = user.getSnsId();
            Authentication preAuth = new UsernamePasswordAuthenticationToken(
                    userEmailOrSnsId,
                    "");
            return preAuth;
        }

    }

    public Authentication makeAuthenticationFrom(User user){

        if (user.getProvider().equals("local")){
            Authentication UsernamePasswordAuthentication = createUsernamePasswordAuthenticationTokenFrom(user);
            return UsernamePasswordAuthentication;
        }
        else{
            Authentication OAuth2Authentication = createOAuth2AuthenticationTokenFrom(user);
            return OAuth2Authentication;
        }
    }

    public Authentication createUsernamePasswordAuthenticationTokenFrom(User user) {
        if (user.getEmail().equals("admin")){
            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }
        else{
            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }
    }

    // AnonymousAuthenticationToken 생성
    public Authentication createAnonymousAuthenticationTokenFrom() {
        return new AnonymousAuthenticationToken(
                "key",
                "anonymousUser",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
        );
    }

    // OAuth2AuthenticationToken 생성
    public Authentication createOAuth2AuthenticationTokenFrom(User user) {
        Map<String, Object> attributes = Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "nickname", user.getNickname(),
                "provider", user.getProvider(),
                "snsId", user.getSnsId()
        );

        OAuth2User oAuth2User = new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email"
        );

        return new OAuth2AuthenticationToken(
                oAuth2User,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                user.getProvider()
        );
    }

    public User getUserInfoFromSecurityContextHolder(){
        Authentication authentication = getAuthenticationFromSecurityContextHolder();
        User user = getUserInfoFrom(authentication);
        return user;
    }

    private User getUserInfoFrom(Authentication authentication){
        Long userId = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken){
            User user = (User) ((UsernamePasswordAuthenticationToken) authentication).getPrincipal();
            userId = user.getId();
        }
        else if (authentication instanceof OAuth2AuthenticationToken){
            Map<String, Object> attributes = ((OAuth2AuthenticationToken) authentication).getPrincipal().getAttributes();
            userId = (Long) attributes.get("id");
        }

        Optional <User> userOpt = userRepository.findById(String.valueOf(userId));
        if (userOpt.isPresent()){
            System.out.println("User " + userOpt.get().getNickname());
            return userOpt.get();
        }
        else{
            System.out.println("User not found");
            return null;
        }
    }

    public void clearAuthenticate() {
        SecurityContextHolder.clearContext();
    }
}
