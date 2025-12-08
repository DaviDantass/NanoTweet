package com.davidantasdev.NanoTweet.controllers;

import com.davidantasdev.NanoTweet.model.Post;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        testUser = userRepository.save(new User("testuser"));
    }

    @Test
    @DisplayName("Should create a post successfully")
    void createPost_WithValidData_ShouldReturn201() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("authorId", testUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Hello World\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Hello World"))
                .andExpect(jsonPath("$.authorUsername").value("testuser"))
                .andExpect(jsonPath("$.type").value("ORIGINAL"));
    }

    @Test
    @DisplayName("Should return 400 when creating post with invalid content")
    void createPost_WithInvalidContent_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("authorId", testUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 404 when creating post with non-existent author")
    void createPost_WithNonExistentAuthor_ShouldReturn400() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("authorId", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get all posts")
    void getAllPosts_ShouldReturnAllPosts() throws Exception {
        postRepository.save(new Post("First post", Post.PostType.ORIGINAL, null, testUser));
        postRepository.save(new Post("Second post", Post.PostType.ORIGINAL, null, testUser));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should get post by id")
    void getPostById_WhenPostExists_ShouldReturn200() throws Exception {
        Post savedPost = postRepository.save(new Post("Test post", Post.PostType.ORIGINAL, null, testUser));

        mockMvc.perform(get("/posts/" + savedPost.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test post"))
                .andExpect(jsonPath("$.authorUsername").value("testuser"));
    }

    @Test
    @DisplayName("Should return 404 when post not found")
    void getPostById_WhenPostNotExists_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/posts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get posts by author")
    void getPostsByAuthor_WhenAuthorExists_ShouldReturn200() throws Exception {
        postRepository.save(new Post("Author post 1", Post.PostType.ORIGINAL, null, testUser));
        postRepository.save(new Post("Author post 2", Post.PostType.ORIGINAL, null, testUser));

        mockMvc.perform(get("/posts/author/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should return 404 when author not found")
    void getPostsByAuthor_WhenAuthorNotExists_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/posts/author/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create repost successfully")
    void createRepost_WithValidData_ShouldReturn201() throws Exception {
        Post originalPost = postRepository.save(new Post("Original post", Post.PostType.ORIGINAL, null, testUser));

        mockMvc.perform(post("/posts/" + originalPost.getId() + "/repost")
                        .param("authorId", testUser.getId().toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("REPOST"))
                .andExpect(jsonPath("$.originalPostId").value(originalPost.getId()));
    }

    @Test
    @DisplayName("Should create quote successfully")
    void createQuote_WithValidData_ShouldReturn201() throws Exception {
        Post originalPost = postRepository.save(new Post("Original post", Post.PostType.ORIGINAL, null, testUser));

        mockMvc.perform(post("/posts/" + originalPost.getId() + "/quote")
                        .param("authorId", testUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"My quote comment\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("QUOTE"))
                .andExpect(jsonPath("$.content").value("My quote comment"))
                .andExpect(jsonPath("$.originalPostId").value(originalPost.getId()));
    }

    @Test
    @DisplayName("Should delete post successfully")
    void deletePost_WhenPostExists_ShouldReturn204() throws Exception {
        Post savedPost = postRepository.save(new Post("To be deleted", Post.PostType.ORIGINAL, null, testUser));

        mockMvc.perform(delete("/posts/" + savedPost.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent post")
    void deletePost_WhenPostNotExists_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/posts/999"))
                .andExpect(status().isNotFound());
    }
}
