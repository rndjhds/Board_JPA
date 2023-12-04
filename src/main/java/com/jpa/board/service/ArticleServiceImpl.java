package com.jpa.board.service;

import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleUpdateDto;
import com.jpa.board.dto.ArticleWithCommentsDto;
import com.jpa.board.dto.UserAccountDto;
import com.jpa.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                        article.getId(),
                        UserAccountDto.builder()
                                .userId(article.getUserAccount().getUserId())
                                .id(article.getUserAccount().getId())
                                .userPassword(article.getUserAccount().getUserPassword())
                                .email(article.getUserAccount().getEmail())
                                .nickname(article.getUserAccount().getNickname())
                                .memo(article.getUserAccount().getMemo())
                                .createdAt(article.getUserAccount().getCreatedAt())
                                .createdBy(article.getUserAccount().getCreatedBy())
                                .modifiedAt(article.getUserAccount().getModifiedAt())
                                .modifiedBy(article.getUserAccount().getModifiedBy())
                                .build(),
                        article.getTitle(),
                        article.getContent(),
                        article.getHashtag(),
                        article.getCreatedAt(),
                        article.getCreatedBy(),
                        article.getModifiedAt(),
                        article.getModifiedBy())).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return null;
    }

    @Override
    public Page<ArticleDto> searchPagingArticles(SearchType title, String searchKeyword, Pageable pageable) {
        return Page.empty();
    }

    @Override
    public void saveArticle(ArticleDto dto) {
    }

    @Override
    public void updateArticle(ArticleDto dto) {
    }

    @Override
    public void deleteArticle(long articleId) {
    }
}
