package com.davidantasdev.NanoTweet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "\\w+")
    @NotNull
    @Size(max = 10)
    private String username;

    @NotNull
    private LocalDateTime createdAt;

    @Deprecated
    public User() {
    }

    public User(String username) {
        this.username = username;
        this.createdAt = LocalDateTime.now();
    }

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
