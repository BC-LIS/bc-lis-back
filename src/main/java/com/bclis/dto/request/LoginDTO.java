package com.bclis.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoginDTO {
    @Schema(description = "Username of the user attempting to log in",
            example = "jdoe",
            type = "string")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Password of the user attempting to log in",
            example = "securePassword123",
            type = "string",
            format = "password")
    @NotBlank(message = "Password is required")
    private String password;
}
