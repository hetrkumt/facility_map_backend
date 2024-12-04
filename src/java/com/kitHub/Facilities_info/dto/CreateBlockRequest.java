package com.kitHub.Facilities_info.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBlockRequest {
    private Long userId;  // 차단할 유저의 ID
}
