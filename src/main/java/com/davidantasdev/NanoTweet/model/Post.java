package com.davidantasdev.NanoTweet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "post")
public class Post {

    public enum PostType {
        ORIGINAL, REPOST, QUOTE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 0, max = 42)
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('ORIGINAL', 'REPOST', 'QUOTE')")
    private PostType type;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_post_id")
    private Post originalPost;

    @NotNull
    private LocalDateTime createdAt;

    @Deprecated
    protected Post() {
    }

    public Post(String content, PostType type, Post originalPost, User author) {
        this.content = content;
        this.type = type;
        this.originalPost = originalPost;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public PostType getType() {
        return type;
    }

    public User getAuthor() {
        return author;
    }

    public Optional<Post> getOriginalPost() {
        return Optional.ofNullable(originalPost);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}