package com.jpa.board.service;

import com.jpa.board.domain.Article;
import com.jpa.board.domain.UserAccount;
import com.jpa.board.domain.constant.SearchType;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleWithCommentsDto;
import com.jpa.board.repository.ArticleRepository;
import com.jpa.board.repository.UserAccountRepository;
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
    private final UserAccountRepository userAccountRepository;


    @Transactional(readOnly = true)
    @Override
    public List<ArticleDto> searchArticles(SearchType title, String searchKeyword) {
        return articleRepository.findAll().stream().map(ArticleDto::from).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId).map(ArticleWithCommentsDto::from).orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다. - articleId:" + articleId));
    }

    @Override
    public Page<ArticleDto> searchPagingArticles(SearchType title, String searchKeyword, Pageable pageable) {

        if (searchKeyword == null || searchKeyword.isEmpty()) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
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

        return articles.map(ArticleDto::from);
    }

    @Override
    public void saveArticle(ArticleDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());
        articleRepository.save(dto.toEntity(userAccount));;
    }

    @Override
    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            if (dto.getTitle() != null) {
                article.setTitle(dto.getTitle());
            }
            if (dto.getContent() != null) {
                article.setContent(dto.getContent());
            }
            article.setHashtag(dto.getHashtag());
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다. {}", dto.getId());
        }

    }

    @Override
    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Override
    public long getArticleCount() {
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ArticleDto> searchPagingArticlesViaHashtag(String hashtag, Pageable pageable) {
        if (hashtag == null || hashtag.isEmpty()) {
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::from);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }
}

