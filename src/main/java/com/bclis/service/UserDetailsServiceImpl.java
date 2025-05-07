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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (userEntity.getIsActive().equals(Boolean.FALSE)) {
            throw new BadCredentialsException("The user is not active");
        }

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

    public UserResponseDTO createUser(CreateUserDTO createUserDTO) {
        RoleEntity roleEntity = roleRepository
                .findByRoleName(EnumRole.valueOf(createUserDTO.getRole()))
                .orElseThrow(() -> new NotFoundException("Role not found"));

        this.validateUdeaEmail(createUserDTO.getEmail());
        this.validateUsername(createUserDTO.getEmail(), createUserDTO.getUsername());

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .name(createUserDTO.getName())
                .lastname(createUserDTO.getLastname())
                .email(createUserDTO.getEmail())
                .role(roleEntity)
                .isActive(true)
                .build();

        return modelMapper.map(userRepository.save(userEntity), UserResponseDTO.class);
    }

    private void validateUsername(String email, String username) {
        String userFromTheEmail = email.split("@")[0];

        if (!userFromTheEmail.equals(username)) {
            throw new InvalidEmailOrUsernameException("The username must match the user's email address.");
        }
    }

    private void validateUdeaEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@udea\\.edu\\.co$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new InvalidEmailOrUsernameException("Invalid email address. Only UdeA email addresses may be used.");
        }
    }
}
