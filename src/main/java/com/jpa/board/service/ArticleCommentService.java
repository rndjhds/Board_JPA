package com.jpa.board.service;

import com.jpa.board.dto.ArticleCommentDto;

import java.util.List;

public interface ArticleCommentService {
    List<ArticleCommentDto> searchArticleComment(Long articleId);
}
