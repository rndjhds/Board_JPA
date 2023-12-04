package com.jpa.board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleUpdateDto {

    private String title;
    private String content;
    private String hashtag;

    @Builder
    public ArticleUpdateDto(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
}
