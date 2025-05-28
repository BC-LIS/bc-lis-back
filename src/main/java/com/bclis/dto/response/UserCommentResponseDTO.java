package com.bclis.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCommentResponseDTO {
    private String username;
    private String name;
    private String lastName;
    private String email;
}