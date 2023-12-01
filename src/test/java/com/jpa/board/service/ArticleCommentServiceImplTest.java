package com.jpa.board.service;

import com.jpa.board.domain.Article;
import com.jpa.board.dto.ArticleCommentDto;
import com.jpa.board.repository.ArticleCommentRepository;
import com.jpa.board.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@DisplayName("비즈니스 로직 - 댓글")
class ArticleCommentServiceImplTest {

    @InjectMocks
    private ArticleCommentServiceImpl sut;


    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    public void givenArticleId_whenSearchingComment_thenReturnsComments() {
        // given
        Long articleId = 1L;
        BDDMockito.given(articleRepository.findById(articleId)).willReturn(Optional.of(
                Article.builder().content("content").title("title").hashtag("#java").build()));
        // when
        List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
        // then
        Assertions.assertThat(articleComments).isNotNull();
        BDDMockito.then(articleRepository).should().findById(articleId);
    }
}