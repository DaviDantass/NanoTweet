package com.davidantasdev.NanoTweet.model.dto;

import com.davidantasdev.NanoTweet.model.Post;
import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String content,
        String authorUsername,
        Post.PostType type,
        Long originalPostId,
        LocalDateTime createdAt
) {
    public PostResponse(Post post) {
        this(
                post.getId(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getType(),
                post.getOriginalPost().map(Post::getId).orElse(null),
                post.getCreatedAt()
        );
    }
}
