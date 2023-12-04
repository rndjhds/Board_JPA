package com.jpa.board.service;

import com.jpa.board.dto.ArticleCommentDto;

import java.util.List;

public interface ArticleCommentService {
    List<ArticleCommentDto> searchArticleComment(Long articleId);

    public void saveArticleComment(ArticleCommentDto dto);

    public void updateArticleComment(ArticleCommentDto dto);

    public void deleteArticleComment(Long articleCommentId);
}
