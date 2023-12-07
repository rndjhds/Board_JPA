package com.jpa.board.service;

import com.jpa.board.domain.Article;
import com.jpa.board.domain.ArticleComment;
import com.jpa.board.domain.UserAccount;
import com.jpa.board.dto.ArticleCommentDto;
import com.jpa.board.dto.UserAccountDto;
import com.jpa.board.repository.ArticleCommentRepository;
import com.jpa.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ArticleCommentDto> searchArticleComment(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId).stream().map(articleComment ->
             ArticleCommentDto.builder()
                    .articleId(articleId)
                    .id(articleComment.getId())
                    .modifiedAt(articleComment.getModifiedAt())
                    .content(articleComment.getContent())
                    .createdBy(articleComment.getCreatedBy())
                    .modifiedBy(articleComment.getModifiedBy())
                    .createdAt(articleComment.getCreatedAt())
                    .userAccountDto(UserAccountDto.builder()
                            .userId(articleComment.getUserAccount().getUserId())
                            .memo(articleComment.getUserAccount().getMemo())
                            .email(articleComment.getUserAccount().getEmail())
                            .id(articleComment.getUserAccount().getId())
                            .userPassword(articleComment.getUserAccount().getUserPassword())
                            .createdAt(articleComment.getUserAccount().getCreatedAt())
                            .modifiedAt(articleComment.getUserAccount().getModifiedAt())
                            .createdBy(articleComment.getUserAccount().getCreatedBy())
                            .nickname(articleComment.getUserAccount().getNickname())
                            .modifiedBy(articleComment.getUserAccount().getModifiedBy())
                            .build())
                    .build()
        ).collect(Collectors.toList());
    }

    @Override
    public void saveArticleComment(ArticleCommentDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.getArticleId());
            UserAccount userAccount = UserAccount.builder()
                    .userId(dto.getUserAccountDto().getUserId())
                    .userPassword(dto.getUserAccountDto().getUserPassword())
                    .email(dto.getUserAccountDto().getEmail())
                    .memo(dto.getUserAccountDto().getMemo())
                    .build();
            ArticleComment articleComment = new ArticleComment(dto.getContent(), article, userAccount);
            articleComment.addArticle(article);
            articleCommentRepository.save(articleComment);
        } catch (EntityNotFoundException e) {
            log.warn("댓글 저장 실패. 댓글의 게시글을 찾을 수 없습니다 - dto: {}", dto);
        }

    }
    @Override
    public void updateArticleComment(ArticleCommentDto dto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.getId());
            if (dto.getContent() != null) { articleComment.setContent(dto.getContent()); }
        } catch (EntityNotFoundException e) {
            log.warn("댓글 업데이트 실패. 댓글을 찾을 수 없습니다 - dto: {}", dto);
        }
    }
    @Override
    public void deleteArticleComment(Long articleCommentId) {
        articleCommentRepository.deleteById(articleCommentId);
    }
}
