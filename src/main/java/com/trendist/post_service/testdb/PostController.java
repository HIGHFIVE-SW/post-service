package com.trendist.post_service.testdb;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

}
