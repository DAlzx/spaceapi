package com.alex.spaceapi.controller;

import com.alex.spaceapi.dto.mapper.UserMapper;
import com.alex.spaceapi.dto.model.UserDto;
import com.alex.spaceapi.model.User;
import com.alex.spaceapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "User services", description = "Related to Parkour API users")
@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(
            summary = "To get user by id"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{userId}")
    User get(@PathVariable Long userId){
        return this.userService.get(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "To add a new user"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping()
    User add(@RequestBody UserDto userDto){
        // -- Check username already exists
        this.userService.getByUsername(userDto.username())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });

        User userToAdd = UserMapper.maptoUser(userDto);
        return this.userService.add(userToAdd)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Operation(
            summary = "To add an existing user",
            description = "Administrator or Manager role required"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{userId}")
    User update(@PathVariable Long userId, @RequestBody UserDto userDto){
        // -- Check if user is found in DB
        User existingUser = this.userService.get(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        User userToUpdate = UserMapper.maptoUser(userDto);
        userToUpdate.setId(existingUser.getId());
        userToUpdate.setCreatedDate(existingUser.getCreatedDate());

        return this.userService.update(userToUpdate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /*
    galad23 -> ROLE USER
    galad233 -> ROLE USER, MANAGER
    galad234 -> ROLE MANAGER
    galad235 -> ADMINISTRATOR
     */

    @Operation(
            summary = "To delete an existing user",
            description = "Administrator role required"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    ResponseEntity delete(@PathVariable Long userId){
        // -- Check if user is found in DB
        User existingUserToDelete = this.userService.get(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        this.userService.delete(existingUserToDelete);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "To get user list",
            description = "Administrator or Manager role required"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    List<User> getAll(){
        return this.userService.getAll();
    }
}
