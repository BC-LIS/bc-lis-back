package com.bclis.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CreateUserDTO {
    @Schema(description = "Username for the new user account",
            example = "jdoe",
            type = "string",
            minLength = 3,
            maxLength = 20)
    @NotBlank(message = "Username is required")
    private String username;

    /**
     * Password for the new user.
     */
    @Schema(description = "Password for the new user account",
            example = "securePassword123",
            type = "string",
            format = "password",
            minLength = 8)
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * First name of the new user.
     */
    @Schema(description = "First name of the new user",
            example = "John",
            type = "string",
            maxLength = 30)
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * Last name of the new user.
     */
    @Schema(description = "Last name of the new user",
            example = "Doe",
            type = "string",
            maxLength = 30)
    @NotBlank(message = "Last name is required")
    private String lastname;

    /**
     * Email address of the new user.
     */
    @Schema(description = "Email address of the new user",
            example = "johndoe@example.com",
            type = "string",
            format = "email")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * Role assigned to the new user.
     */
    @Schema(description = "Role assigned to the user, defining access permissions",
            example = "USER",
            type = "string",
            allowableValues = {"ADMIN", "USER", "GUEST"})
    @NotBlank(message = "Role is required")
    private String role;
}
