package com.jpa.board.service;

import com.jpa.board.domain.Article;
import com.jpa.board.domain.UserAccount;
import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.ArticleCommentDto;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleWithCommentsDto;
import com.jpa.board.dto.UserAccountDto;
import com.jpa.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
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
        return articleRepository.findById(articleId).map(article -> ArticleWithCommentsDto.builder()
                        .articleCommentDtos(article.getArticleComments().stream().map(articleComment -> ArticleCommentDto.builder()
                                .content(articleComment.getContent())
                                .createdAt(articleComment.getCreatedAt())
                                .createdBy(articleComment.getCreatedBy())
                                .id(articleComment.getId())
                                .modifiedBy(articleComment.getModifiedBy())
                                .articleId(articleComment.getArticle().getId())
                                .userAccountDto(UserAccountDto.builder().userId(articleComment.getUserAccount().getUserId())
                                        .memo(articleComment.getUserAccount().getMemo())
                                        .userPassword(articleComment.getUserAccount().getUserPassword())
                                        .email(articleComment.getUserAccount().getEmail())
                                        .nickname(articleComment.getUserAccount().getNickname())
                                        .id(articleComment.getUserAccount().getId())
                                        .modifiedBy(articleComment.getUserAccount().getModifiedBy())
                                        .modifiedAt(articleComment.getUserAccount().getModifiedAt())
                                        .createdBy(articleComment.getUserAccount().getCreatedBy())
                                        .createdAt(articleComment.getUserAccount().getCreatedAt())
                                        .build())
                                .modifiedAt(articleComment.getModifiedAt())
                                .build()).collect(Collectors.toList()))
                        .id(article.getId())
                        .modifiedBy(article.getModifiedBy())
                        .modifiedAt(article.getModifiedAt())
                        .createdBy(article.getCreatedBy())
                        .createdAt(article.getCreatedAt())
                        .content(article.getContent())
                        .hashtag(article.getHashtag())
                        .title(article.getTitle())
                        .build())
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. - articleId:" + articleId));
    }

    @Override
    public Page<ArticleDto> searchPagingArticles(SearchType title, String searchKeyword, Pageable pageable) {

        if (searchKeyword == null || searchKeyword.isEmpty()) {
            return articleRepository.findAll(pageable).map(article -> ArticleDto.builder()
                    .id(article.getId())
                    .createdAt(article.getCreatedAt())
                    .createdBy(article.getCreatedBy())
                    .content(article.getContent())
                    .modifiedBy(article.getModifiedBy())
                    .modifiedAt(article.getModifiedAt())
                    .hashtag(article.getHashtag())
                    .title(article.getTitle())
                    .userAccountDto(UserAccountDto.builder().userId(article.getUserAccount().getUserId())
                            .email(article.getUserAccount().getEmail())
                            .modifiedAt(article.getUserAccount().getModifiedAt())
                            .modifiedBy(article.getUserAccount().getModifiedBy())
                            .id(article.getUserAccount().getId())
                            .createdBy(article.getUserAccount().getCreatedBy())
                            .createdAt(article.getUserAccount().getCreatedAt())
                            .memo(article.getUserAccount().getMemo())
                            .nickname(article.getUserAccount().getNickname())
                            .userPassword(article.getUserAccount().getUserPassword())
                            .build())
                    .build());
        }

        Page<Article> articles = null;
        switch (title) {
            case TITLE:
                articles = articleRepository.findByTitleContaining(searchKeyword, pageable);
                break;
            case CONTENT:
                articles = articleRepository.findByContentContaining(searchKeyword, pageable);
                break;
            case ID:
                articles = articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable);
                break;
            case NICKNAME:
                articles = articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable);
                break;
            case HASHTAG:
                articles = articleRepository.findByHashtag("#" + searchKeyword, pageable);
        }

        return articles.map(article -> ArticleDto.builder()
                .id(article.getId())
                .createdAt(article.getCreatedAt())
                .createdBy(article.getCreatedBy())
                .content(article.getContent())
                .modifiedBy(article.getModifiedBy())
                .modifiedAt(article.getModifiedAt())
                .hashtag(article.getHashtag())
                .title(article.getTitle())
                .userAccountDto(UserAccountDto.builder().userId(article.getUserAccount().getUserId())
                        .email(article.getUserAccount().getEmail())
                        .modifiedAt(article.getUserAccount().getModifiedAt())
                        .modifiedBy(article.getUserAccount().getModifiedBy())
                        .id(article.getUserAccount().getId())
                        .createdBy(article.getUserAccount().getCreatedBy())
                        .createdAt(article.getUserAccount().getCreatedAt())
                        .memo(article.getUserAccount().getMemo())
                        .nickname(article.getUserAccount().getNickname())
                        .userPassword(article.getUserAccount().getUserPassword())
                        .build())
                .build());
    }

    @Override
    public void saveArticle(ArticleDto dto) {
        articleRepository.save(Article.builder()
                .content(dto.getContent())
                .title(dto.getTitle())
                .hashtag(dto.getHashtag())
                .userAccount(UserAccount.builder()
                        .userId(dto.getUserAccountDto().getUserId())
                        .userPassword(dto.getUserAccountDto().getUserPassword())
                        .email(dto.getUserAccountDto().getEmail())
                        .nickname(dto.getUserAccountDto().getNickname())
                        .memo(dto.getUserAccountDto().getMemo())
                        .build())
                .build());
    }

    @Override
    public void updateArticle(ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.getId());
            if (dto.getTitle() != null) {
                article.setTitle(dto.getTitle());
            }
            if (dto.getContent() != null) {
                article.setContent(dto.getContent());
            }
            article.setHashtag(dto.getHashtag());
        }catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. {}", dto.getId());
        }

    }

    @Override
    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
