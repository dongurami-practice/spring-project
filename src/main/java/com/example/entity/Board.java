package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "POST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 40, nullable = false)
    @Size(min = 2, max = 40)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Builder
    public Board(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //==내용 수정==//
    public void update(String title, String content) {
        this.title = (title != null && !title.isBlank()) ? title : this.title;
        this.content = (content != null && !content.isBlank()) ? content : this.content;
    }
}
