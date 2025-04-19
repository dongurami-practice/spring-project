package com.example.board.dto;

import com.example.entity.Board;

public class PostInfoDto {
    private Long id;
    private String title;
    private String content;

    public PostInfoDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static PostInfoDto from(Board board) {
        return new PostInfoDto(board.getId(), board.getTitle(), board.getContent());
    }
}