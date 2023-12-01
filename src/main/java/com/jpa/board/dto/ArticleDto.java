package com.jpa.board.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class ArticleDto {

    private String title;
    private String content;
    private String hashtag;
    private LocalDateTime createdAt;
    private String createdBy;

    @Builder
    public ArticleDto(String title, String content, String hashtag, LocalDateTime createdAt, String createdBy) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }
}
