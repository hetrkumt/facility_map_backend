package com.kitHub.Facilities_info.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocalLoginRequest {
    private String Email;
    private String password;
}
