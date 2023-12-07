package com.jpa.board.dto.response;

import com.jpa.board.dto.ArticleWithCommentsDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ArticleWithCommentResponse {
    private Long id;
    private String title;
    private String content;
    private String hashtag;
    private LocalDateTime createdAt;
    private String email;
    private String nickname;
    private List<ArticleCommentResponse> articleCommentResponses;

    @Builder
    public ArticleWithCommentResponse(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String email, String nickname, List<ArticleCommentResponse> articleCommentResponses) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.createdAt = createdAt;
        this.email = email;
        this.nickname = nickname;
        this.articleCommentResponses = articleCommentResponses;
    }

    public static ArticleWithCommentResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.getUserAccountDto().getNickname();
        if (nickname == null || nickname.isEmpty()) {
            nickname = dto.getUserAccountDto().getUserId();
        }

        return ArticleWithCommentResponse.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .hashtag(dto.getHashtag())
                .createdAt(dto.getCreatedAt())
                .email(dto.getUserAccountDto().getEmail())
                .nickname(nickname)
                .articleCommentResponses(
                        dto.getArticleCommentDtos().stream().map(ArticleCommentResponse::from).collect(Collectors.toList())
                ).build();
    }
}
