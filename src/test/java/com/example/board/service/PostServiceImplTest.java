package com.example.board.service;

import com.example.board.dto.*;
import com.example.entity.Board;
import com.example.board.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.data.domain.*;

import java.lang.reflect.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PostServiceImplTest {

    private PostServiceImpl service;

    // 스텁이 기록하거나 리턴할 값들
    private Board savedArg;
    private Board saveReturn;
    private Pageable lastPageable;
    private Page<Board> pageReturn;
    private Long lastFindId;
    private Board findReturn;
    private Board lastDeleted;

    @BeforeEach
    void setUp() {
        // 동적 프록시로 PostRepository 스텁 생성
        PostRepository stubRepo = (PostRepository) Proxy.newProxyInstance(
                PostRepository.class.getClassLoader(),
                new Class[]{ PostRepository.class },
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("save".equals(name)) {
                        savedArg = (Board) args[0];
                        return saveReturn;
                    }
                    if ("findAll".equals(name)
                            && args.length == 1
                            && args[0] instanceof Pageable) {
                        lastPageable = (Pageable) args[0];
                        return pageReturn;
                    }
                    if ("findById".equals(name)) {
                        lastFindId = (Long) args[0];
                        return Optional.ofNullable(findReturn);
                    }
                    if ("delete".equals(name)) {
                        lastDeleted = (Board) args[0];
                        return null;
                    }
                    throw new UnsupportedOperationException("Stub not implemented: " + method);
                }
        );

        service = new PostServiceImpl(stubRepo);
    }

    @Test
    void savePost_shouldReturnGeneratedId_andPassThroughDto() {
        // 준비: saveReturn 보드를 id=42로 세팅
        Board b = Board.builder().title("T").content("C").build();
        ReflectionTestUtils.setField(b, "id", 42L);
        saveReturn = b;

        // 실행
        PostSaveDto dto = new PostSaveDto("T", "C");
        Long id = service.savePost(dto);

        // 검증
        assertEquals(42L, id);
        assertNotNull(savedArg);
        assertEquals("T", savedArg.getTitle());
        assertEquals("C", savedArg.getContent());
    }

    @Test
    void getAllPosts_shouldReturnMappedDtos_andRecordPageable() {
        // 준비: pageReturn 에 Board 리스트 넣기
        Board b1 = Board.builder().title("t1").content("x").build();
        Board b2 = Board.builder().title("t2").content("y").build();
        ReflectionTestUtils.setField(b1, "id", 1L);
        ReflectionTestUtils.setField(b2, "id", 2L);
        pageReturn = new PageImpl<>(List.of(b1, b2));

        Pageable pg = PageRequest.of(0, 10);

        // 실행
        Page<PostPagingDto> result = service.getAllPosts(pg);

        // 검증
        assertSame(pg, lastPageable, "Service should pass through the pageable");
        assertEquals(2, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("t2", result.getContent().get(1).getTitle());
    }

    @Test
    void getPostById_whenFound_shouldReturnDto_andRecordId() {
        // 준비
        Board b = Board.builder().title("A").content("B").build();
        ReflectionTestUtils.setField(b, "id", 7L);
        findReturn = b;

        // 실행
        PostInfoDto dto = service.getPostById(7L);

        // 검증
        assertEquals(7L, lastFindId);
        assertEquals(7L, dto.getId());
        assertEquals("A", dto.getTitle());
        assertEquals("B", dto.getContent());
    }

    @Test
    void getPostById_whenNotFound_shouldThrow() {
        findReturn = null;  // Optional.empty
        assertThrows(IllegalArgumentException.class,
                () -> service.getPostById(99L));
        assertEquals(99L, lastFindId);
    }

    @Test
    void updatePost_whenFound_shouldModifyEntity() {
        // 준비: findReturn 보드
        Board b = Board.builder().title("old").content("old").build();
        ReflectionTestUtils.setField(b, "id", 5L);
        findReturn = b;

        // 실행
        PostUpdateDto dto = new PostUpdateDto(Optional.of("newT"), Optional.of("newC"));
        service.updatePost(5L, dto);

        // 검증: 엔티티의 update()가 작동했는지
        assertEquals("newT", b.getTitle());
        assertEquals("newC", b.getContent());
    }

    @Test
    void updatePost_whenNotFound_shouldThrow() {
        findReturn = null;
        assertThrows(IllegalArgumentException.class,
                () -> service.updatePost(123L, new PostUpdateDto(Optional.empty(), Optional.empty())));
    }

    @Test
    void deletePost_whenFound_shouldRecordDeletedEntity() {
        // 준비
        Board b = Board.builder().title("X").content("Y").build();
        ReflectionTestUtils.setField(b, "id", 8L);
        findReturn = b;

        // 실행
        service.deletePost(8L);

        // 검증
        assertEquals(b, lastDeleted);
    }

    @Test
    void deletePost_whenNotFound_shouldThrow() {
        findReturn = null;
        assertThrows(IllegalArgumentException.class,
                () -> service.deletePost(77L));
    }

    @Test
    void getPostInfo_shouldDelegateToGetPostById() {
        Board b = Board.builder().title("Z").content("W").build();
        ReflectionTestUtils.setField(b, "id", 9L);
        findReturn = b;

        PostInfoDto one = service.getPostById(9L);
        PostInfoDto two = service.getPostInfo(9L);

        assertEquals(one.getId(), two.getId());
        assertEquals(one.getContent(), two.getContent());
    }
}

