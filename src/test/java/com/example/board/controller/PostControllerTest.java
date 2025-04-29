package com.example.board.controller;

import com.example.board.dto.*;
import com.example.board.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PostControllerTest {

    private PostController controller;
    private StubPostService service;

    @BeforeEach
    void setUp() {
        service = new StubPostService();
        controller = new PostController(service);
    }

    @Test
    void createPost_returnsConfiguredId_andDelegatesDto() {
        // 준비: stub에 반환값 설정
        service.saveReturn = 123L;

        PostSaveDto dto = new PostSaveDto("t", "c");
        Long result = controller.createPost(dto);

        // 검증
        assertEquals(123L, result);
        // 컨트롤러가 제대로 service.savePost(dto)를 불렀는지 확인
        assertSame(dto, service.lastSaveDto);
    }

    @Test
    void getAllPosts_returnsStubPage() {
        // 준비
        PostPagingDto p1 = new PostPagingDto(1L, "one");
        Page<PostPagingDto> stubPage = new PageImpl<>(List.of(p1));
        service.pageReturn = stubPage;

        Pageable pg = PageRequest.of(0, 10);
        Page<PostPagingDto> result = controller.getAllPosts(pg);

        // 검증
        assertSame(stubPage, result);
        assertEquals(1, result.getTotalElements());
        assertEquals("one", result.getContent().get(0).getTitle());
        // 컨트롤러가 올바른 pageable을 전달했는지 확인
        assertEquals(pg, service.lastPageable);
    }

    @Test
    void getPost_returnsConfiguredDto() {
        // 준비
        PostInfoDto info = new PostInfoDto(5L, "hi", "hello");
        service.infoReturn = info;

        PostInfoDto result = controller.getPost(5L);

        assertSame(info, result);
        assertEquals(5L, service.lastFindId);
    }

    @Test
    void updatePost_delegatesToService() {
        PostUpdateDto dto = new PostUpdateDto(Optional.of("u"), Optional.of("v"));

        controller.updatePost(7L, dto);

        assertEquals(7L, service.lastUpdateId);
        assertSame(dto, service.lastUpdateDto);
    }

    @Test
    void deletePost_delegatesToService() {
        controller.deletePost(9L);
        assertEquals(9L, service.lastDeleteId);
    }

    // ============================
    // 내부 Stub 구현체
    // ============================
    static class StubPostService implements PostService {
        // 테스트용 저장소 / 반환값
        Long saveReturn;
        PostSaveDto lastSaveDto;

        Page<PostPagingDto> pageReturn;
        Pageable lastPageable;

        PostInfoDto infoReturn;
        Long lastFindId;

        Long lastUpdateId;
        PostUpdateDto lastUpdateDto;

        Long lastDeleteId;

        @Override
        public Long savePost(PostSaveDto dto) {
            this.lastSaveDto = dto;
            return saveReturn;
        }

        @Override
        public Page<PostPagingDto> getAllPosts(Pageable pageable) {
            this.lastPageable = pageable;
            return pageReturn;
        }

        @Override
        public PostInfoDto getPostById(Long id) {
            this.lastFindId = id;
            return infoReturn;
        }

        @Override
        public void updatePost(Long id, PostUpdateDto dto) {
            this.lastUpdateId = id;
            this.lastUpdateDto = dto;
        }

        @Override
        public void deletePost(Long id) {
            this.lastDeleteId = id;
        }

        @Override
        public PostInfoDto getPostInfo(Long id) {
            // PostController는 getPostById만 쓰므로 안 써도 됩니다.
            return getPostById(id);
        }
    }
}

