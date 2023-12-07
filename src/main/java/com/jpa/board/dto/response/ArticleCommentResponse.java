package com.jpa.board.dto.response;

import com.jpa.board.dto.ArticleCommentDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleCommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String email;
    private String nickname;

    @Builder
    public ArticleCommentResponse(Long id, String content, LocalDateTime createdAt, String email, String nickname) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.email = email;
        this.nickname = nickname;
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.getUserAccountDto().getNickname();
        if (nickname == null || nickname.isEmpty()) {
            nickname = dto.getUserAccountDto().getUserId();
        }

        return ArticleCommentResponse.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .email(dto.getUserAccountDto().getEmail())
                .nickname(nickname)
                .build();
    }
}
