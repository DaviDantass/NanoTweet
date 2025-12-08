package com.davidantasdev.NanoTweet;

import com.davidantasdev.NanoTweet.model.User;
import com.davidantasdev.NanoTweet.repository.PostRepository;
import com.davidantasdev.NanoTweet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("should create a new user")
    void createUser() throws Exception {
        String userJson = "{\"username\": \"alice\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("should get all users")
    void getAllUsers() throws Exception {
        userRepository.save(new User("bob"));
        userRepository.save(new User("charlie"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value("bob"))
                .andExpect(jsonPath("$[1].username").value("charlie"));
    }

    @Test
    @DisplayName("should get user by id")
    void getUserById() throws Exception {
        User user = userRepository.save(new User("dave"));

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value("dave"));
    }

    @Test
    @DisplayName("should return 404 when user not found")
    void getUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should update user")
    void updateUser() throws Exception {
        User user = userRepository.save(new User("eve"));
        String updateJson = "{\"username\": \"evelyn\"}";

        mockMvc.perform(put("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value("evelyn"));
    }

    @Test
    @DisplayName("should delete user")
    void deleteUser() throws Exception {
        User user = userRepository.save(new User("frank"));

        mockMvc.perform(delete("/users/" + user.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should return 400 for invalid username")
    void createUserWithInvalidUsername() throws Exception {
        String invalidJson = "{\"username\": \"this-is-too-long-username\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
