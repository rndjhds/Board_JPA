package com.jpa.board.service;

import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArticleService {
    List<ArticleDto> searchArticles(SearchType title, String searchKeyword);

    ArticleDto searchArticle(long l);

    Page<ArticleDto> searchPagingArticles(SearchType title, String searchKeyword);

    void saveArticle(ArticleDto dto);

    void updateArticle(long l, ArticleUpdateDto dto);

    void deleteArticle(long l);
}
