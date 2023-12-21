package com.jpa.board.service;

import com.jpa.board.domain.constant.SearchType;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleWithCommentsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    List<ArticleDto> searchArticles(SearchType title, String searchKeyword);

    public ArticleWithCommentsDto getArticleWithComments(Long articleId);

    Page<ArticleDto> searchPagingArticles(SearchType searchType, String searchKeyword, Pageable pageable);

    void saveArticle(ArticleDto dto);

    void updateArticle(Long articleId, ArticleDto dto);

    void deleteArticle(long articleId);

    long getArticleCount();

    Page<ArticleDto> searchPagingArticlesViaHashtag(String hashtag, Pageable pageable);

    List<String> getHashtags();

    ArticleDto getArticle(Long articleId);
}