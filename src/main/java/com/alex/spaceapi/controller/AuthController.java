package com.alex.spaceapi.controller;

import com.alex.spaceapi.security.JwtUtil;
import com.alex.spaceapi.security.MyUserDetailsService;
import com.alex.spaceapi.dto.mapper.UserMapper;
import com.alex.spaceapi.dto.model.AuthenticationRequestDto;
import com.alex.spaceapi.dto.model.UserDto;
import com.alex.spaceapi.model.User;
import com.alex.spaceapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Tag(name = "Authentication services", description = "Related to authentication and security in Parkour API")
@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;

    private final MyUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(
            summary = "Service for authentication login"
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthenticationRequestDto authenticationRequestDto){
        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDto.username(),
                            authenticationRequestDto.password()
                    )
            );
        } catch (Exception e){
            throw new RuntimeException("Incorrect username or password", e);
        }

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequestDto.username());
        final String jwt = this.jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());

        return ResponseEntity.ok(Map.of("accessToken", jwt));
    }

    @Operation(
            summary = "Service for authentication register"
    )
    @PostMapping("/register")
    public User register(@RequestBody UserDto userDto){

        // -- Check username already exists
        this.userService.getByUsername(userDto.username())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });


        User userToAdd = UserMapper.maptoUser(userDto);
        userToAdd.setPassword(passwordEncoder.encode(userDto.password()));
        return this.userService.add(userToAdd)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
