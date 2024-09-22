package com.bclis.service;

import com.bclis.dto.request.CreateUserDTO;
import com.bclis.dto.request.LoginDTO;
import com.bclis.dto.response.AuthResponseDTO;
import com.bclis.persistence.entity.RoleEntity;
import com.bclis.persistence.entity.UserEntity;
import com.bclis.persistence.entity.enums.EnumRole;
import com.bclis.persistence.repository.RoleRepository;
import com.bclis.persistence.repository.UserRepository;
import com.bclis.utils.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(userEntity.getRole().getRoleName().name())));

        userEntity.getRole().getPermissions()
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                true,
                true,
                true,
                true,
                authorityList);
    }

    public AuthResponseDTO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.generateAccessToken(authentication);

        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return AuthResponseDTO.builder()
                .token(accessToken)
                .message("Authentication successful")
                .username(username)
                .name(userEntity.getName())
                .lastname(userEntity.getLastname())
                .role(userEntity.getRole().getRoleName().name())
                .build();
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    public void createUser(CreateUserDTO createUserDTO) {

        RoleEntity roleEntity = roleRepository
                .findByRoleName(EnumRole.valueOf(createUserDTO.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found"));

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .name(createUserDTO.getName())
                .lastname(createUserDTO.getLastname())
                .email(createUserDTO.getEmail())
                .role(roleEntity)
                .build();

        userRepository.save(userEntity);
    }
}
