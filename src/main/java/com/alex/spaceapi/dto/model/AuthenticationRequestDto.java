package com.alex.spaceapi.dto.model;

public record AuthenticationRequestDto(
        String username,
        String password
){}