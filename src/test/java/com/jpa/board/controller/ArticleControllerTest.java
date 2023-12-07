package com.jpa.board.controller;

import com.jpa.board.config.SecurityConfig;
import com.jpa.board.dto.ArticleWithCommentsDto;
import com.jpa.board.dto.UserAccountDto;
import com.jpa.board.service.ArticleService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest(ArticleController.class) // 특정 컨트롤러만 실행시키도록 추가
@Import(SecurityConfig.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("{view} {GET} 게시글 리스트 {게시글} 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // given
        BDDMockito.given(articleService.searchPagingArticles(eq(null), eq(null), BDDMockito.any(Pageable.class))).willReturn(Page.empty());
        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"));
        BDDMockito.then(articleService).should().searchPagingArticles(eq(null), eq(null), BDDMockito.any(Pageable.class));
        // then
    }

    @DisplayName("{view} {GET} 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // given
        Long articleId = 1L;
        BDDMockito.given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles/" + articleId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("article"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articleComments"));
        BDDMockito.then(articleService).should().getArticle(articleId);
        // then
    }

    @Disabled("개발중")
    @DisplayName("{view} {GET} 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearcghView_thenReturnsArticleSearchView() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles/search"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles/search"));
        // then
    }

    @Disabled("개발중")
    @DisplayName("{view} {GET} 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles/search-hashtag"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles/search-hashtag"));
        // then
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.builder()
                .id(1L)
                .userAccountDto(createUserAccountDto())
                .articleCommentDtos(new ArrayList<>())
                .title("title")
                .hashtag("#java")
                .createdAt(LocalDateTime.now())
                .createdBy("uno")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("uno")
                .content("content")
                .build();
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.builder()
                .id(1L)
                .userId("Uno")
                .userPassword("password")
                .email("uno@gmail.com")
                .nickname("Uno")
                .memo("memo")
                .createdAt(LocalDateTime.now())
                .createdBy("uno")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("uno")
                .build();
    }

}