package com.jpa.board.service;

import com.jpa.board.dto.ArticleCommentDto;
import com.jpa.board.repository.ArticleCommentRepository;
import com.jpa.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ArticleCommentDto> searchArticleComment(Long articleId) {
        return articleRepository.findById(articleId).get().getArticleComments()
                .stream()
                .map(articleComment ->
                        ArticleCommentDto.builder()
                                .createdAt(articleComment.getCreatedAt())
                                .createdBy(articleComment.getCreatedBy())
                                .content(articleComment.getContent())
                                .modifiedAt(articleComment.getModifiedAt())
                                .modifiedBy(articleComment.getModifiedBy()).build()).collect(Collectors.toList());
    }

    @Override
    public void saveArticleComment(ArticleCommentDto dto) {

    }
    @Override
    public void updateArticleComment(ArticleCommentDto dto) {
    }
    @Override
    public void deleteArticleComment(Long articleCommentId) {
    }
}
