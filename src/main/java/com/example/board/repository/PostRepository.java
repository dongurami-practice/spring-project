package com.example.board.repository;

import com.example.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Board, Long> {
    // 기본적인 CRUD 메서드는 JpaRepository가 자동 제공
}
