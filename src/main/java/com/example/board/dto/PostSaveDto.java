package com.example.board.dto;

import com.example.entity.Board;
import jakarta.validation.constraints.NotBlank;


public record PostSaveDto (@NotBlank(message = "제목을 입력해주세요") String title,
                           @NotBlank(message = "내용을 입력해주세요") String content)
{
    public Board toEntity() {
        return Board.builder().title(title).content(content).build();
    }
}
