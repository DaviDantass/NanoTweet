package com.davidantasdev.NanoTweet.service;

import com.davidantasdev.NanoTweet.exception.ResourceNotFoundException;
import com.davidantasdev.NanoTweet.model.Post;
import com.davidantasdev.NanoTweet.model.User;
import com.davidantasdev.NanoTweet.model.dto.PostRequest;
import com.davidantasdev.NanoTweet.model.dto.PostResponse;
import com.davidantasdev.NanoTweet.repository.PostRepository;
import com.davidantasdev.NanoTweet.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findAll() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(PostResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<PostResponse> findById(Long id) {
        return postRepository.findById(id)
                .map(PostResponse::new);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> findByAuthor(Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
        
        return postRepository.findByAuthor(author, Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(PostResponse::new)
                .toList();
    }

    public PostResponse createPost(Long authorId, PostRequest request) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));

        Post post = new Post(request.content(), Post.PostType.ORIGINAL, null, author);
        Post savedPost = postRepository.save(post);
        return new PostResponse(savedPost);
    }

    public PostResponse createRepost(Long authorId, Long originalPostId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
        
        Post originalPost = postRepository.findById(originalPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + originalPostId));

        Post repost = new Post("", Post.PostType.REPOST, originalPost, author);
        Post savedRepost = postRepository.save(repost);
        return new PostResponse(savedRepost);
    }

    public PostResponse createQuote(Long authorId, Long originalPostId, PostRequest request) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));
        
        Post originalPost = postRepository.findById(originalPostId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + originalPostId));

        Post quote = new Post(request.content(), Post.PostType.QUOTE, originalPost, author);
        Post savedQuote = postRepository.save(quote);
        return new PostResponse(savedQuote);
    }

    public boolean delete(Long id) {
        return postRepository.findById(id)
                .map(post -> {
                    postRepository.delete(post);
                    return true;
                })
                .orElse(false);
    }
}
