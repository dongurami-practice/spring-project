package com.example.board.dto;

import com.example.entity.Board;
import lombok.Getter;

@Getter
public class PostPagingDto {
    private Long id;
    private String title;

    public PostPagingDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static PostPagingDto from(Board board) {
        return new PostPagingDto(board.getId(), board.getTitle());
    }
}
