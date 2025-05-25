package com.bclis.controller;

import com.bclis.dto.request.UserFiltersDto;
import com.bclis.dto.response.UserResponseDTO;
import com.bclis.persistence.entity.enums.EnumRole;
import com.bclis.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) EnumRole role,
            @RequestParam(required = false) Boolean isActive) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        UserFiltersDto userFiltersDto = new UserFiltersDto(name, role, isActive);
        return ResponseEntity.ok(userDetailsService.getAllUsers(pageable, userFiltersDto));
    }

    @PatchMapping("/isActive")
    public ResponseEntity<UserResponseDTO> updateUserStatus(@RequestParam String username, @RequestParam Boolean isActive) {
        return ResponseEntity.ok(userDetailsService.updateUserStatus(username, isActive));
    }
}
