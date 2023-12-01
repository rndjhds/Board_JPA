package com.jpa.board.service;

import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleUpdateDto;
import com.jpa.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ArticleDto> searchArticles(SearchType title, String searchKeyword) {
        return articleRepository.findAll()
                .stream()
                .map(article -> new ArticleDto(
                        article.getTitle(),
                        article.getContent(),
                        article.getHashtag(),
                        article.getCreatedAt(),
                        article.getCreatedBy())).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleDto searchArticle(long l) {
        return null;
    }

    @Override
    public Page<ArticleDto> searchPagingArticles(SearchType title, String searchKeyword) {
        return Page.empty();
    }

    @Override
    public void saveArticle(ArticleDto dto) {
    }

    @Override
    public void updateArticle(long l, ArticleUpdateDto dto) {
    }

    @Override
    public void deleteArticle(long l) {
    }
}
