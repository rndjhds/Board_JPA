package com.jpa.board.controller;

import com.jpa.board.config.SecurityConfig;
import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.ArticleWithCommentsDto;
import com.jpa.board.dto.UserAccountDto;
import com.jpa.board.service.ArticleService;
import com.jpa.board.service.PaginationService;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest(ArticleController.class) // 특정 컨트롤러만 실행시키도록 추가
@Import(SecurityConfig.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("{view} {GET} 게시글 리스트 {게시글} 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // given
        BDDMockito.given(articleService.searchPagingArticles(eq(null), eq(null), BDDMockito.any(Pageable.class))).willReturn(Page.empty());
        BDDMockito.given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(Arrays.nonNullElementsIn(new Integer[]{0, 1, 2, 3, 4}));
        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("paginationBarNumbers"));
        BDDMockito.then(articleService).should().searchPagingArticles(eq(null), eq(null), BDDMockito.any(Pageable.class));
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
        // then
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = Arrays.nonNullElementsIn(new Integer[]{0, 1, 2, 3, 4});
        BDDMockito.given(articleService.searchPagingArticles(null, null, pageable)).willReturn(Page.empty());
        BDDMockito.given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        MockMvcRequestBuilders.get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"))
                .andExpect(MockMvcResultMatchers.model().attribute("paginationBarNumbers", barNumbers));
        BDDMockito.then(articleService).should().searchPagingArticles(null, null, pageable);
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

    @DisplayName("[view][GET] 게시글 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        // given
        Long articleId = 1L;
        long totalCount = 1L;

        BDDMockito.given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        BDDMockito.given(articleService.getArticleCount()).willReturn(totalCount);

        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles/" + articleId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("article"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articleComments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articleComments"))
                .andExpect(MockMvcResultMatchers.model().attribute("totalCount", totalCount));

        BDDMockito.then(articleService).should().getArticle(articleId);
        BDDMockito.then(articleService).should().getArticleCount();
        // then
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyWord_whenSearchingArticleView_thenReturnsArticleView() throws Exception {
        // given
        SearchType searchType = SearchType.TITLE;
        String searchVale = "title";

        BDDMockito.given(articleService.searchPagingArticles(eq(searchType), eq(searchVale), any(Pageable.class))).willReturn(Page.empty());
        BDDMockito.given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(Arrays.nonNullElementsIn(new Integer[]{0, 1, 2, 3, 4}));


        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles/")
                        .queryParam("searchType", searchType.name())
                        .queryParam("searchValue", searchVale)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("searchTypes"));

        BDDMockito.then(articleService).should().searchPagingArticles(eq(searchType), eq(searchVale), any(Pageable.class));
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
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

    @DisplayName("{view} {GET} 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // given
        BDDMockito.given(articleService.searchPagingArticlesViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
        List<String> expectedHashtags = new ArrayList<>();
        expectedHashtags.add("#java");
        expectedHashtags.add("#spring");
        expectedHashtags.add("#boot");
        BDDMockito.given(articleService.getHashtags()).willReturn(expectedHashtags);
        BDDMockito.given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(Arrays.nonNullElementsIn(new Integer[]{0, 1, 2, 3, 4}));
        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles/search-hashtag"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name(("articles/search-hashtag")))
                .andExpect(MockMvcResultMatchers.model().attribute("articles", Page.empty()))
                .andExpect(MockMvcResultMatchers.model().attributeExists("hashtags"))
                .andExpect(MockMvcResultMatchers.model().attribute("searchType", SearchType.HASHTAG))
                .andExpect(MockMvcResultMatchers.model().attributeExists("paginationBarNumbers"));
        BDDMockito.then(articleService).should().searchPagingArticlesViaHashtag(eq(null), any(Pageable.class));
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
        BDDMockito.then(articleService).should().getHashtags();
        // then
    }

    @DisplayName("{view} {GET} 게시글 해시태그 검색 페이지 - 정상 호출, 해시태그 입력")
    @Test
    public void givenHashtag_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        // given
        String hashtag = "#java";
        BDDMockito.given(articleService.searchPagingArticlesViaHashtag(eq(hashtag), any(Pageable.class))).willReturn(Page.empty());
        List<String> expectedHashtags = new ArrayList<>();
        expectedHashtags.add("#java");
        expectedHashtags.add("#spring");
        expectedHashtags.add("#boot");
        BDDMockito.given(articleService.getHashtags()).willReturn(expectedHashtags);
        BDDMockito.given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(Arrays.nonNullElementsIn(new Integer[]{0, 1, 2, 3, 4}));

        // when
        mvc.perform(MockMvcRequestBuilders.get("/articles/search-hashtag")
                        .queryParam("searchValue", hashtag)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name(("articles/search-hashtag")))
                .andExpect(MockMvcResultMatchers.model().attribute("articles", Page.empty()))
                .andExpect(MockMvcResultMatchers.model().attribute("hashtags", expectedHashtags))
                .andExpect(MockMvcResultMatchers.model().attribute("searchType", SearchType.HASHTAG))
                .andExpect(MockMvcResultMatchers.model().attributeExists("paginationBarNumbers"));
        BDDMockito.then(articleService).should().searchPagingArticlesViaHashtag(eq(hashtag), any(Pageable.class));
        BDDMockito.then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
        BDDMockito.then(articleService).should().getHashtags();
        // then
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(1L, createUserAccountDto(), new ArrayList<>(), "title", "content", "#java", LocalDateTime.now(), "uno", LocalDateTime.now(), "uno");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "pw",
                "uno@mail.com",
                "Uno",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

}