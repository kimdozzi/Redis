package com.example.performance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PerformanceController {

    @GetMapping("/api/posts/{postsId}")
    public String getPosts(@PathVariable Long postsId) {
        return "performance test - postsId: " + postsId;
    }
}
