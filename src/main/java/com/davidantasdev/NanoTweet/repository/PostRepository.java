package com.davidantasdev.NanoTweet.repository;

import com.davidantasdev.NanoTweet.model.Post;
import com.davidantasdev.NanoTweet.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(User author, Sort sort);
}
