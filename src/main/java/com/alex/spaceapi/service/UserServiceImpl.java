package com.alex.spaceapi.service;

import com.alex.spaceapi.model.User;
import com.alex.spaceapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> get(Long userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public Optional<User> add(User userToAdd) {
        // -- Update user object with create and update dates
        userToAdd.setUpdatedDate(LocalDateTime.now());
        userToAdd.setCreatedDate(LocalDateTime.now());

        return Optional.of(this.userRepository.save(userToAdd));
    }

    @Override
    public Optional<User> update(User userToUpdate) {
        // -- Update user object with update date
        userToUpdate.setUpdatedDate(LocalDateTime.now());

        return Optional.of(this.userRepository.save(userToUpdate));
    }

    @Override
    public void delete(User user) {
        this.userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.ofNullable(this.userRepository.findByUsername(username));
    }
}
