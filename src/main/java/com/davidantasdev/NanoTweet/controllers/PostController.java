package com.davidantasdev.NanoTweet.controllers;

import com.davidantasdev.NanoTweet.model.dto.PostRequest;
import com.davidantasdev.NanoTweet.model.dto.PostResponse;
import com.davidantasdev.NanoTweet.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return postService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostResponse>> getPostsByAuthor(@PathVariable Long authorId) {
        try {
            List<PostResponse> posts = postService.findByAuthor(authorId);
            return ResponseEntity.ok(posts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestParam Long authorId,
            @Valid @RequestBody PostRequest request) {
        try {
            PostResponse response = postService.createPost(authorId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{originalPostId}/repost")
    public ResponseEntity<PostResponse> createRepost(
            @PathVariable Long originalPostId,
            @RequestParam Long authorId) {
        try {
            PostResponse response = postService.createRepost(authorId, originalPostId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{originalPostId}/quote")
    public ResponseEntity<PostResponse> createQuote(
            @PathVariable Long originalPostId,
            @RequestParam Long authorId,
            @Valid @RequestBody PostRequest request) {
        try {
            PostResponse response = postService.createQuote(authorId, originalPostId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        return postService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
