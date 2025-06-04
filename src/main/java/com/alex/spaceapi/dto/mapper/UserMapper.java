package com.alex.spaceapi.dto.mapper;

import com.alex.spaceapi.dto.model.UserDto;
import com.alex.spaceapi.model.User;

public class UserMapper {
    public static User maptoUser(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setRoles(userDto.roles());

        return user;
    }
}
