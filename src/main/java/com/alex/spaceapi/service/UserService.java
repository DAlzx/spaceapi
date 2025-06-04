package com.alex.spaceapi.service;

import com.alex.spaceapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    /**
     * To get user from DB
     * @param userId the user id to get
     * @return user found with id provided
     */
    Optional<User> get(Long userId);

    /**
     * To add new user to DB
     * @param userToAdd the user to add
     * @return new user added to DB
     */
    Optional<User> add(User userToAdd);

    /**
     * To update existing user in DB
     * @param userToUpdate the user to update
     * @return existing user updated
     */
    Optional<User> update(User userToUpdate);

    /**
     * To delete existing user from DB
     * @param user the user to delete
     */
    void delete(User user);

    /**
     * To get all users
     * @return all users in DB
     */
    List<User> getAll();

    /**
     * To get user from DB via username
     * @param username the username to find
     * @return user found with username provided
     */
    Optional<User> getByUsername(String username);

}
