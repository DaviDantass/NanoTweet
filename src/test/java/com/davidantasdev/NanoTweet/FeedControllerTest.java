package com.davidantasdev.NanoTweet;

import com.davidantasdev.NanoTweet.model.Post;
import com.davidantasdev.NanoTweet.model.User;
import com.davidantasdev.NanoTweet.repository.PostRepository;
import com.davidantasdev.NanoTweet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FeedControllerTest {

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
    @DisplayName("should return a feed with posts")
    void x1() throws Exception {

        User alexandre = userRepository.save(
                new User(null, "alexandre")
        );

        postRepository.save(new Post("A vida é bela.", Post.PostType.ORIGINAL, null, alexandre));
        postRepository.save(new Post("Aproveita cada instante.", Post.PostType.ORIGINAL, null, alexandre));

        mockMvc.perform(get("/feed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].authorUsername").value("alexandre"))
                .andExpect(jsonPath("$[0].type").value(Post.PostType.ORIGINAL.name()))
                .andExpect(jsonPath("$[1].id").isNotEmpty())
                .andExpect(jsonPath("$[1].authorUsername").value("alexandre"))
                .andExpect(jsonPath("$[1].type").value(Post.PostType.ORIGINAL.name()));
    }
}
