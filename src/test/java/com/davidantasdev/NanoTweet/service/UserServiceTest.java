package com.davidantasdev.NanoTweet.service;

import com.davidantasdev.NanoTweet.model.User;
import com.davidantasdev.NanoTweet.model.dto.UserRequest;
import com.davidantasdev.NanoTweet.model.dto.UserResponse;
import com.davidantasdev.NanoTweet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("testuser");
        mockUser.setUsername("testuser");
    }

    @Test
    @DisplayName("Should return all users")
    void findAll_ShouldReturnAllUsers() {
        User user1 = new User("user1");
        User user2 = new User("user2");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponse> result = userService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).username()).isEqualTo("user1");
        assertThat(result.get(1).username()).isEqualTo("user2");
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Should find user by id")
    void findById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        Optional<UserResponse> result = userService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().username()).isEqualTo("testuser");
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void findById_WhenUserNotExists_ShouldReturnEmpty() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<UserResponse> result = userService.findById(999L);

        assertThat(result).isEmpty();
        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("Should create user successfully")
    void create_WithValidRequest_ShouldCreateUser() {
        UserRequest request = new UserRequest("newuser");
        User savedUser = new User("newuser");
        when(userRepository.findAll()).thenReturn(List.of());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userService.create(request);

        assertThat(result.username()).isEqualTo("newuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when username already exists")
    void create_WithDuplicateUsername_ShouldThrowException() {
        UserRequest request = new UserRequest("testuser");
        when(userRepository.findAll()).thenReturn(List.of(mockUser));

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username already exists");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update user successfully")
    void update_WhenUserExists_ShouldUpdateUser() {
        UserRequest request = new UserRequest("updateduser");
        User existingUser = new User(1L, "olduser");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findAll()).thenReturn(List.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        Optional<UserResponse> result = userService.update(1L, request);

        assertThat(result).isPresent();
        assertThat(result.get().username()).isEqualTo("updateduser");
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should return empty when updating non-existent user")
    void update_WhenUserNotExists_ShouldReturnEmpty() {
        UserRequest request = new UserRequest("newname");
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<UserResponse> result = userService.update(999L, request);

        assertThat(result).isEmpty();
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void delete_WhenUserExists_ShouldReturnTrue() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        boolean result = userService.delete(1L);

        assertThat(result).isTrue();
        verify(userRepository).delete(mockUser);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent user")
    void delete_WhenUserNotExists_ShouldReturnFalse() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = userService.delete(999L);

        assertThat(result).isFalse();
        verify(userRepository, never()).delete(any());
    }
}
