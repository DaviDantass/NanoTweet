package com.davidantasdev.NanoTweet.controllers;

import com.davidantasdev.NanoTweet.model.dto.PostResponse;
import com.davidantasdev.NanoTweet.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FeedController {

    private final PostService postService;

    public FeedController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/feed")
    public List<PostResponse> feed() {
        return postService.findAll();
    }
}
