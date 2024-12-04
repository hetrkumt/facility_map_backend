package com.kitHub.Facilities_info.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddLocalUserRequest {
    private String email;
    private String password;
    private String nickname;
}