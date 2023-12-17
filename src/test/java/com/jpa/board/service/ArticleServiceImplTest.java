package com.jpa.board.service;

import com.jpa.board.domain.Article;
import com.jpa.board.domain.UserAccount;
import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleWithCommentsDto;
import com.jpa.board.dto.UserAccountDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@ExtendWith(MockitoExtension.class)
@DisplayName("비즈니스 로직 - 게시글")
class ArticleServiceImplTest {

    @InjectMocks
    private ArticleServiceImpl sut; // Mock을 주입할 대상
    @Mock
    private ArticleRepository articleRepository; // 그외 Mock

    @Test
    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // given
        Pageable pageable = Pageable.ofSize(20);
        BDDMockito.given(articleRepository.findAll(pageable)).willReturn(Page.empty());
        // when
        Page<ArticleDto> articles = sut.searchPagingArticles(null, null, pageable);
        // 제목, 본문, ID, 닉네임, 해시태그
        // then
        Assertions.assertThat(articles).isEmpty();
        BDDMockito.then(articleRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        BDDMockito.given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchPagingArticles(searchType, searchKeyword, pageable);

        // Then
        Assertions.assertThat(articles).isEmpty();
        BDDMockito.then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @Test
    @DisplayName("게시글 조회하면, 게시글을 반환한다.")
    public void givenId_whenSearchingArticle_thenReturnArticleList() {
        // given
        Long articleId = 1L;
        Article article = createArticle();
        BDDMockito.given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleWithCommentsDto dto = sut.getArticle(articleId);

        // Then
        Assertions.assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        BDDMockito.then(articleRepository).should().findById(articleId);
    }

    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {
        // Given
        Long articleId = 0L;
        BDDMockito.given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        Throwable t = catchThrowable(() -> sut.getArticle(articleId));
        Assertions.assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다. - articleId:" + articleId);
        BDDMockito.then(articleRepository).should().findById(articleId);
    }

    @Test
    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    public void givenSearchParameterAndPage_whenSearchingArticles_thenReturnArticleList() {
        // given

        // when
        List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, ID, 닉네임, 해시태그
        // then
        Assertions.assertThat(articles).isNotNull();
    }

    @Test
    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    public void givenArticleInfo_whenSavingArticle_thenSavedArticle() {
        // given
        ArticleDto dto = createArticleDto();
        BDDMockito.given(articleRepository.save(ArgumentMatchers.any(Article.class))).willReturn(createArticle());
        // when
        sut.saveArticle(dto);
        // then
        BDDMockito.then(articleRepository).should().save(ArgumentMatchers.any(Article.class));
    }

    @Test
    @DisplayName("게시글의 ID와 수정정보를 입력하면, 게시글을 수정한다.")
    public void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {

        // given
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        BDDMockito.given(articleRepository.getReferenceById(dto.getId())).willReturn(article);

        // when
        sut.updateArticle(dto);

        // then
        Assertions.assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.getTitle())
                .hasFieldOrPropertyWithValue("content", dto.getContent())
                .hasFieldOrPropertyWithValue("hashtag", dto.getHashtag());
        BDDMockito.then(articleRepository).should().getReferenceById(dto.getId());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        BDDMockito.given(articleRepository.getReferenceById(dto.getId())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(dto);

        // Then
        BDDMockito.then(articleRepository).should().getReferenceById(dto.getId());
    }

    @Test
    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long articleId = 1L;
        BDDMockito.willDoNothing().given(articleRepository).deleteById(articleId);

        // When
        sut.deleteArticle(1L);

        // Then
        BDDMockito.then(articleRepository).should().deleteById(articleId);
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환하다.")
    @Test
    void givenNoSearchParameter_whenSearchingArticleViaHashtag_thenReturnEmptyPage() {
        // given
        Pageable pageable = Pageable.ofSize(20);

        // when
        Page<ArticleDto> articles = sut.searchPagingArticlesViaHashtag(null, pageable);

        // then
        Assertions.assertThat(articles).isEqualTo(Page.empty(pageable));
        BDDMockito.then(articleRepository).shouldHaveNoInteractions();
    }

    @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환하다.")
    @Test
    void givenHashtag_whenSearchingArticleViaHashtag_thenReturnArticlesPage() {
        // given
        Pageable pageable = Pageable.ofSize(20);
        String hashtag = "#java";
        BDDMockito.given(articleRepository.findByHashtag(hashtag, pageable)).willReturn(Page.empty(pageable));
        // when
        Page<ArticleDto> articles = sut.searchPagingArticlesViaHashtag(hashtag, pageable);

        // then
        Assertions.assertThat(articles).isEqualTo(Page.empty(pageable));
        BDDMockito.then(articleRepository).should().findByHashtag(hashtag, pageable);
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags() {
        // Given
        List<String> expectedHashtags = new ArrayList<>();
        expectedHashtags.add("#java");
        expectedHashtags.add("#spring");
        expectedHashtags.add("#boot");

        BDDMockito.given(articleRepository.findAllDistinctHashtags()).willReturn(expectedHashtags);

        // When
        List<String> actualHashtags = sut.getHashtags();

        // Then
        Assertions.assertThat(actualHashtags).isEqualTo(expectedHashtags);
        BDDMockito.then(articleRepository).should().findAllDistinctHashtags();
    }

    @DisplayName("게시글 수를 조회하면, 게시글 수를 반환한다")
    @Test
    void givenNothing_whenCountingArticles_thenReturnsArticleCount() {
        // Given
        long expected = 0L;
        BDDMockito.given(articleRepository.count()).willReturn(expected);

        // When
        long actual = sut.getArticleCount();

        // Then
        Assertions.assertThat(actual).isEqualTo(expected);
        BDDMockito.then(articleRepository).should().count();
    }


    private UserAccount createUserAccount() {
        return UserAccount.of("uno", "password", "uno@email.com", "Uno", null);
    }

    private Article createArticle() {
        return Article.of(createUserAccount(), "title", "content", "#java");
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content", "#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
        return ArticleDto.of(1L, createUserAccountDto(), title, content, hashtag, LocalDateTime.now(), "Uno", LocalDateTime.now(), "Uno");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}