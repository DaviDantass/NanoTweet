package com.davidantasdev.NanoTweet.service;

import com.davidantasdev.NanoTweet.exception.ResourceNotFoundException;
import com.davidantasdev.NanoTweet.model.Post;
import com.davidantasdev.NanoTweet.model.User;
import com.davidantasdev.NanoTweet.model.dto.PostRequest;
import com.davidantasdev.NanoTweet.model.dto.PostResponse;
import com.davidantasdev.NanoTweet.repository.PostRepository;
import com.davidantasdev.NanoTweet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private User mockUser;
    private Post mockPost;

    @BeforeEach
    void setUp() {
        mockUser = new User("testuser");
        mockPost = new Post("Test content", Post.PostType.ORIGINAL, null, mockUser);
    }

    @Test
    @DisplayName("Should return all posts ordered by creation date descending")
    void findAll_ShouldReturnAllPostsOrdered() {
        // Given
        Post post1 = new Post("First post", Post.PostType.ORIGINAL, null, mockUser);
        Post post2 = new Post("Second post", Post.PostType.ORIGINAL, null, mockUser);
        when(postRepository.findAll(any(Sort.class))).thenReturn(List.of(post2, post1));

        // When
        List<PostResponse> result = postService.findAll();

        // Then
        assertThat(result).hasSize(2);
        verify(postRepository).findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Test
    @DisplayName("Should find post by id")
    void findById_WhenPostExists_ShouldReturnPost() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        // When
        Optional<PostResponse> result = postService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().content()).isEqualTo("Test content");
        verify(postRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when post not found")
    void findById_WhenPostNotExists_ShouldReturnEmpty() {
        // Given
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<PostResponse> result = postService.findById(999L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find posts by author")
    void findByAuthor_WhenAuthorExists_ShouldReturnPosts() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(postRepository.findByAuthor(eq(mockUser), any(Sort.class)))
                .thenReturn(List.of(mockPost));

        // When
        List<PostResponse> result = postService.findByAuthor(1L);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).authorUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Should throw exception when author not found")
    void findByAuthor_WhenAuthorNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> postService.findByAuthor(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Author not found");
    }

    @Test
    @DisplayName("Should create post successfully")
    void createPost_WithValidData_ShouldCreatePost() {
        // Given
        PostRequest request = new PostRequest("New post");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        // When
        PostResponse result = postService.createPost(1L, request);

        // Then
        assertThat(result).isNotNull();
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("Should throw exception when creating post with non-existent author")
    void createPost_WhenAuthorNotExists_ShouldThrowException() {
        // Given
        PostRequest request = new PostRequest("New post");
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> postService.createPost(999L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Author not found");
        verify(postRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create repost successfully")
    void createRepost_WithValidData_ShouldCreateRepost() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        // When
        PostResponse result = postService.createRepost(1L, 1L);

        // Then
        assertThat(result).isNotNull();
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("Should throw exception when original post not found for repost")
    void createRepost_WhenOriginalPostNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> postService.createRepost(1L, 999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Post not found");
    }

    @Test
    @DisplayName("Should create quote successfully")
    void createQuote_WithValidData_ShouldCreateQuote() {
        // Given
        PostRequest request = new PostRequest("Quote comment");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        // When
        PostResponse result = postService.createQuote(1L, 1L, request);

        // Then
        assertThat(result).isNotNull();
        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("Should delete post successfully")
    void delete_WhenPostExists_ShouldReturnTrue() {
        // Given
        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

        // When
        boolean result = postService.delete(1L);

        // Then
        assertThat(result).isTrue();
        verify(postRepository).delete(mockPost);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent post")
    void delete_WhenPostNotExists_ShouldReturnFalse() {
        // Given
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        boolean result = postService.delete(999L);

        // Then
        assertThat(result).isFalse();
        verify(postRepository, never()).delete(any());
    }
}
