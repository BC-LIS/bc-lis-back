package com.bclis.service;

import com.bclis.dto.request.CreateUserDTO;
import com.bclis.dto.request.LoginDTO;
import com.bclis.dto.response.AuthResponseDTO;
import com.bclis.dto.response.UserResponseDTO;
import com.bclis.persistence.entity.RoleEntity;
import com.bclis.persistence.entity.UserEntity;
import com.bclis.persistence.entity.enums.EnumRole;
import com.bclis.persistence.repository.RoleRepository;
import com.bclis.persistence.repository.UserRepository;
import com.bclis.utils.exceptions.InvalidEmailOrUsernameException;
import com.bclis.utils.exceptions.NotFoundException;
import com.bclis.utils.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private RoleEntity roleTechnical;
    private UserEntity userActive;
    private UserEntity userInactive;

    @BeforeEach
    void setup() {
        roleTechnical = RoleEntity.builder()
                .id(1L)
                .roleName(EnumRole.TECHNICAL)
                .permissions(Collections.emptySet())
                .build();

        userActive = UserEntity.builder()
                .id(1L)
                .username("activeUser")
                .password("encodedPassword")
                .email("activeUser@udea.edu.co")
                .isActive(true)
                .role(roleTechnical)
                .build();

        userInactive = UserEntity.builder()
                .id(2L)
                .username("inactiveUser")
                .password("encodedPassword")
                .email("inactiveUser@udea.edu.co")
                .isActive(false)
                .role(roleTechnical)
                .build();
    }

    @Test
    void loadUserByUsername_shouldLoadActiveUser() {
        when(userRepository.findByUsername("activeUser")).thenReturn(Optional.of(userActive));

        var userDetails = userDetailsService.loadUserByUsername("activeUser");

        assertEquals("activeUser", userDetails.getUsername());
        assertTrue(userDetails.isEnabled());
        assertFalse(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_shouldThrowForInactiveUser() {
        when(userRepository.findByUsername("inactiveUser")).thenReturn(Optional.of(userInactive));

        BadCredentialsException ex = assertThrows(BadCredentialsException.class,
                () -> userDetailsService.loadUserByUsername("inactiveUser"));

        assertEquals("The user is not active", ex.getMessage());
    }

    @Test
    void login_shouldReturnAuthResponseDTO() {
        LoginDTO loginDTO = LoginDTO.builder()
                .username("activeUser")
                .password("rawPassword")
                .build();

        when(userRepository.findByUsername("activeUser")).thenReturn(Optional.of(userActive));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtils.generateAccessToken(any(Authentication.class))).thenReturn("token123");

        AuthResponseDTO response = userDetailsService.login(loginDTO);

        assertEquals("activeUser", response.getUsername());
        assertEquals("token123", response.getToken());
        assertEquals("Authentication successful", response.getMessage());
    }

    @Test
    void createUser_shouldThrowIfRoleNotFound() {
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .role("TECHNICAL")
                .email("test@udea.edu.co")
                .username("test")
                .build();

        when(roleRepository.findByRoleName(EnumRole.TECHNICAL)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userDetailsService.createUser(createUserDTO));

        assertEquals("Role not found", ex.getMessage());
    }

    @Test
    void createUser_shouldThrowIfEmailInvalid() {
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .role("TECHNICAL")
                .email("invalidemail@gmail.com") // No UdeA email
                .username("invalidemail")
                .build();

        when(roleRepository.findByRoleName(EnumRole.TECHNICAL))
                .thenReturn(Optional.of(roleTechnical));

        InvalidEmailOrUsernameException ex = assertThrows(InvalidEmailOrUsernameException.class,
                () -> userDetailsService.createUser(createUserDTO));

        assertEquals("Invalid email address. Only UdeA email addresses may be used.", ex.getMessage());
    }

    @Test
    void createUser_shouldThrowIfUsernameDoesNotMatchEmail() {
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .role("TECHNICAL")
                .email("user@udea.edu.co")
                .username("otherUser")
                .build();

        when(roleRepository.findByRoleName(EnumRole.TECHNICAL))
                .thenReturn(Optional.of(roleTechnical));

        InvalidEmailOrUsernameException ex = assertThrows(InvalidEmailOrUsernameException.class,
                () -> userDetailsService.createUser(createUserDTO));

        assertEquals("The username must match the user's email address.", ex.getMessage());
    }

    @Test
    void createUser_shouldSaveUser() {
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .role("TECHNICAL")
                .email("user@udea.edu.co")
                .username("user")
                .password("rawPassword")
                .name("Name")
                .lastname("LastName")
                .build();

        when(roleRepository.findByRoleName(EnumRole.TECHNICAL))
                .thenReturn(Optional.of(roleTechnical));

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        UserEntity savedUser = UserEntity.builder()
                .id(1L)
                .username("user")
                .password("encodedPassword")
                .email("user@udea.edu.co")
                .role(roleTechnical)
                .isActive(true)
                .name("Name")
                .lastname("LastName")
                .build();

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUser);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUsername("user");
        when(modelMapper.map(any(UserEntity.class), eq(UserResponseDTO.class))).thenReturn(responseDTO);

        UserResponseDTO result = userDetailsService.createUser(createUserDTO);

        assertEquals("user", result.getUsername());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}

