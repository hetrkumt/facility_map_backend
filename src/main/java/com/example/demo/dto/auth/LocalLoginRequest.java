package com.example.demo.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocalLoginRequest {
    private String Email;
    private String password;
    private String Nickname;
    private String Provider;
}
