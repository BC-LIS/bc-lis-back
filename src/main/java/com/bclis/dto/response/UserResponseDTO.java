package com.bclis.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private String username;
    private String name;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private Boolean isActive;
    private RoleResponseDTO role;
}
