package com.jpa.board.service;

import com.jpa.board.domain.Article;
import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.ArticleDto;
import com.jpa.board.dto.ArticleUpdateDto;
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

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("비즈니스 로직 - 게시글")
class ArticleServiceImplTest {

    @InjectMocks
    private ArticleServiceImpl sut; // Mock을 주입할 대상
    @Mock
    private ArticleRepository articleRepository; // 그외 Mock

    @Test
    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    public void givenSearchParameter_whenSearchingArticles_thenReturnArticleList() {
        // given

        // when
        List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, ID, 닉네임, 해시태그
        // then
        Assertions.assertThat(articles).isNotNull();
    }

    @Test
    @DisplayName("게시글 조회하면, 게시글을 반환한다.")
    public void givenId_whenSearchingArticle_thenReturnArticleList() {
        // given

        // when
        ArticleDto article = sut.searchArticle(1L); // 제목, 본문, ID, 닉네임, 해시태그
        // then
        Assertions.assertThat(article).isNotNull();
    }

    @Test
    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    public void givenSearchParameterAndPage_whenSearchingArticles_thenReturnArticleList() {
        // given

        // when
        Page<ArticleDto> articles = sut.searchPagingArticles(SearchType.TITLE, "search keyword"); // 제목, 본문, ID, 닉네임, 해시태그
        // then
        Assertions.assertThat(articles).isNotNull();
    }

    @Test
    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    public void givenArticleInfo_whenSavingArticle_thenSavedArticle() {
        // given
        ArticleDto dto =ArticleDto.builder().createdAt(LocalDateTime.now()).createdBy("Uno").content("content").title("title").hashtag("#java").build();
        BDDMockito.given(articleRepository.save(ArgumentMatchers.any(Article.class))).willReturn(null);
        // when
        sut.saveArticle(dto);
        // then
        BDDMockito.then(articleRepository).should().save(ArgumentMatchers.any(Article.class));
    }

    @Test
    @DisplayName("게시글의 ID와 수정정보를 입력하면, 게시글을 수정한다.")
    public void givenArticleIdAndModifiedInfo_whenModifingArticle_thenModifiedArticle() {
        // given
        BDDMockito.given(articleRepository.save(ArgumentMatchers.any(Article.class))).willReturn(null);
        // when
        sut.updateArticle(1L, ArticleUpdateDto.builder().content("content").title("title").hashtag("#java").build());

        // then
        BDDMockito.then(articleRepository).should().save(ArgumentMatchers.any(Article.class));
    }

    @Test
    @DisplayName("게시글의 ID 입력하면, 게시글을 삭제한다.")
    public void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // given
        BDDMockito.willDoNothing().given(articleRepository).delete(ArgumentMatchers.any(Article.class));
        // when
        sut.deleteArticle(1L);

        // then
        BDDMockito.then(articleRepository).should().delete(ArgumentMatchers.any(Article.class));
    }
}