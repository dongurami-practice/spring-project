package com.example.board.service;

import com.example.entity.Board;
import com.example.board.dto.PostInfoDto;
import com.example.board.dto.PostPagingDto;
import com.example.board.dto.PostSaveDto;
import com.example.board.dto.PostUpdateDto;
import com.example.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl {
    private final PostRepository postRepository;

    //게시물 생성
    @Transactional
    public Long savePost(PostSaveDto postSaveDto) {
        Board board = postSaveDto.toEntity();
        return postRepository.save(board).getId();
    }

    //게시물 전체 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<PostPagingDto> getAllPosts(Pageable pageable) {
        // pageable을 그대로 전달해서 페이징 처리 및 정렬을 적용한 게시물 반환
        return postRepository.findAll(pageable)
                .map(PostPagingDto::from);  // Board 엔티티를 PostPagingDto로 변환
    }

    // 게시물 상세 조회
    @Transactional
    public PostInfoDto getPostById(Long id) {
        Board board = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. id=" + id));
        return PostInfoDto.from(board);
    }

    //게시물 수정
    public void updatePost(Long id, PostUpdateDto postUpdateDto) {
        Board board = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. id=" + id));
    }

    //게시물 삭제
    public void deletePost(Long id) {
        Board board = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. id=" + id));
    }
}
