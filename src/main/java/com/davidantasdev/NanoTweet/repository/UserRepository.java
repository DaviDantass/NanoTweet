package com.davidantasdev.NanoTweet.repository;

import com.davidantasdev.NanoTweet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
