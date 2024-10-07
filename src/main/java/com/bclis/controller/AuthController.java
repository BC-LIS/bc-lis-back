package com.bclis.controller;

import com.bclis.dto.request.CreateUserDTO;
import com.bclis.dto.request.LoginDTO;
import com.bclis.dto.response.AuthResponseDTO;
import com.bclis.service.UserDetailsServiceImpl;
import com.bclis.utils.constans.ApiDescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = ApiDescription.AUTH_CONTROLLER_DESCRIPTION)
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    @Operation(summary = "Login user", description = ApiDescription.LOGIN_DESCRIPTION)
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(userDetailsService.login(loginDTO));
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = ApiDescription.REGISTER_DESCRIPTION)
    public ResponseEntity<String> createUser(@RequestBody CreateUserDTO createUserDTO) {
        userDetailsService.createUser(createUserDTO);
        return ResponseEntity.ok("User created");
    }
}
