package com.davidantasdev.NanoTweet.service;

import com.davidantasdev.NanoTweet.exception.ResourceNotFoundException;
import com.davidantasdev.NanoTweet.model.User;
import com.davidantasdev.NanoTweet.model.dto.UserRequest;
import com.davidantasdev.NanoTweet.model.dto.UserResponse;
import com.davidantasdev.NanoTweet.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse);
    }

    public UserResponse create(UserRequest request) {
        validateUsernameUniqueness(request.username());
        
        User user = new User(request.username());
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    public Optional<UserResponse> update(Long id, UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    if (!user.getUsername().equals(request.username())) {
                        validateUsernameUniqueness(request.username());
                    }
                    user.setUsername(request.username());
                    User updatedUser = userRepository.save(user);
                    return toResponse(updatedUser);
                });
    }

    public boolean delete(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    private void validateUsernameUniqueness(String username) {
        userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Username already exists: " + username);
                });
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }
}
