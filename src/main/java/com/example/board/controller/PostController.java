package com.example.board.controller;

import com.example.board.dto.PostInfoDto;
import com.example.board.dto.PostPagingDto;
import com.example.board.dto.PostSaveDto;
import com.example.board.dto.PostUpdateDto;
import com.example.board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //게시물 생성
    @PostMapping
    public Long createPost(@RequestBody @Valid PostSaveDto postSaveDto) {
        return postService.savePost(postSaveDto);
    }

    // 게시물 전체 조회 (페이징)
    public Page<PostPagingDto> getAllPosts(Pageable pageable) {
        return postService.getAllPosts(pageable);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    public PostInfoDto getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PatchMapping("/{id}")
    public void updatePost(@PathVariable Long id, @RequestBody @Valid PostUpdateDto postUpdateDto) {
        postService.updatePost(id, postUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }
}
