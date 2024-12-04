package com.kitHub.Facilities_info.dto.community;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {
    private String title;
    private String content;
}
