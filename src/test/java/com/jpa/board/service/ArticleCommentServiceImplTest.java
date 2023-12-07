package com.jpa.board.service;

import com.jpa.board.domain.Article;
import com.jpa.board.domain.ArticleComment;
import com.jpa.board.domain.UserAccount;
import com.jpa.board.dto.ArticleCommentDto;
import com.jpa.board.dto.UserAccountDto;
import com.jpa.board.repository.ArticleCommentRepository;
import com.jpa.board.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedList;
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
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
        // Given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content");
        List<ArticleComment> list = new LinkedList<>();
        list.add(expected);
        BDDMockito.given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(list);

        // When
        List<ArticleCommentDto> actual = sut.searchArticleComment(articleId);

        // Then
        Assertions.assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content", expected.getContent());
        BDDMockito.then(articleCommentRepository).should().findByArticle_Id(articleId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        BDDMockito.given(articleRepository.getReferenceById(dto.getArticleId())).willReturn(createArticle());
        BDDMockito.given(articleCommentRepository.save(ArgumentMatchers.any(ArticleComment.class))).willReturn(null);

        // When
        sut.saveArticleComment(dto);

        // Then
        BDDMockito.then(articleCommentRepository).should().save(ArgumentMatchers.any(ArticleComment.class));
    }

    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
    @Test
    void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        BDDMockito.given(articleRepository.getReferenceById(dto.getArticleId())).willThrow(EntityNotFoundException.class);

        // When
        sut.saveArticleComment(dto);

        // Then
        BDDMockito.then(articleRepository).should().getReferenceById(dto.getArticleId());
        BDDMockito.then(articleCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
    @Test
    void givenArticleCommentInfo_whenUpdatingArticleComment_thenUpdatesArticleComment() {
        // Given
        String oldContent = "content";
        String updatedContent = "댓글";
        ArticleComment articleComment = createArticleComment(oldContent);
        ArticleCommentDto dto = createArticleCommentDto(updatedContent);
        BDDMockito.given(articleCommentRepository.getReferenceById(dto.getId())).willReturn(articleComment);

        // When
        sut.updateArticleComment(dto);

        // Then
        Assertions.assertThat(articleComment.getContent())
                .isNotEqualTo(oldContent)
                .isEqualTo(updatedContent);
        BDDMockito.then(articleCommentRepository).should().getReferenceById(dto.getId());
    }

    @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무 것도 안 한다.")
    @Test
    void givenNonexistentArticleComment_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        BDDMockito.given(articleCommentRepository.getReferenceById(dto.getId())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticleComment(dto);

        // Then
        BDDMockito.then(articleCommentRepository).should().getReferenceById(dto.getId());
    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
        // Given
        Long articleCommentId = 1L;
        BDDMockito.willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);

        // When
        sut.deleteArticleComment(articleCommentId);

        // Then
        BDDMockito.then(articleCommentRepository).should().deleteById(articleCommentId);
    }


    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.builder()
                .id(1L)
                .articleId(1L)
                .userAccountDto(createUserAccountDto())
                .content(content)
                .createdAt(LocalDateTime.now())
                .createdBy("Uno")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("uno")
                .build();
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.builder()
                .id(1L)
                .userId("uno")
                .userPassword("password")
                .email("uno@mail.com")
                .nickname("Uno")
                .memo("This is memo")
                .createdAt(LocalDateTime.now())
                .createdBy("uno")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("uno").build();
    }

    private ArticleComment createArticleComment(String content) {
        return ArticleComment.builder()
                .article(
                        Article.builder()
                                .userAccount(createUserAccount())
                                .title("title")
                                .content("content")
                                .hashtag("hashtag")
                                .build())
                .userAccount(createUserAccount())
                .content(content)
                .build();
    }

    private UserAccount createUserAccount() {
        return UserAccount.builder()
                .userId("uno")
                .userPassword("password")
                .email("uno@email.com")
                .nickname("Uno")
                .memo(null)
                .build();
    }

    private Article createArticle() {
        return Article.builder()
                .userAccount(createUserAccount())
                .title("title")
                .content("content")
                .hashtag("#java")
                .build();
    }

}