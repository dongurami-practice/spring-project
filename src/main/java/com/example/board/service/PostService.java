package com.example.board.service;

import com.example.board.dto.PostInfoDto;
import com.example.board.dto.PostPagingDto;
import com.example.board.dto.PostSaveDto;
import com.example.board.dto.PostUpdateDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface PostService {

    //게시글 등록
    Long savePost(@Valid PostSaveDto postSaveDto);

    //게시글 전체 조회
    Page<PostPagingDto> getAllPosts(Pageable pageable);

    //게시물 상세 조회
    PostInfoDto getPostById(Long id);

    //게시글 수정
    void updatePost(Long id, @Valid PostUpdateDto postUpdateDto);

    //게시글 삭제
    void deletePost(Long id);

    //게시글 1개 조회
    PostInfoDto getPostInfo(Long id);
}
