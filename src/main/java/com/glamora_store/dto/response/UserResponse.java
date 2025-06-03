package com.glamora_store.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;          // thường có id trả về
    private String name;
    private String email;
    private String phoneNumber;
    private String image;
}
