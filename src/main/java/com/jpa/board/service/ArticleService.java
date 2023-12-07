package com.jpa.board.service;

import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleUpdateDto;
import com.jpa.board.dto.ArticleWithCommentsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    List<ArticleDto> searchArticles(SearchType title, String searchKeyword);

    public ArticleWithCommentsDto getArticle(Long articleId);

    Page<ArticleDto> searchPagingArticles(SearchType searchType, String searchKeyword, Pageable pageable);

    void saveArticle(ArticleDto dto);

    void updateArticle(ArticleDto dto);

    void deleteArticle(long articleId);
}
