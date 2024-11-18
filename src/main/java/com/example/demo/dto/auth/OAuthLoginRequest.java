package com.example.demo.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OAuthLoginRequest {
    private String SnsId;
    private String Nickname;
    private String Provider;
}
