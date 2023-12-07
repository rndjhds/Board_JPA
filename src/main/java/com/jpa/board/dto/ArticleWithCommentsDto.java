package com.jpa.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ArticleWithCommentsDto {
    private  Long id;
    private UserAccountDto userAccountDto;
    private List<ArticleCommentDto> articleCommentDtos;
    private String title;
    private String content;
    private String hashtag;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    @Builder
    public ArticleWithCommentsDto(Long id, UserAccountDto userAccountDto, List<ArticleCommentDto> articleCommentDtos, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        this.id = id;
        this.userAccountDto = userAccountDto;
        this.articleCommentDtos = articleCommentDtos;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }
}
