package com.jpa.board.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Disabled("Spring Data Rest 통합테스는 불필요하므로 제외시킴")
@DisplayName("Data Rest 테스트")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    public void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articles"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
        // test
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    public void givenArticleNumber_whenRequestingArticles_thenReturnsArticleJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articles/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
        // test
    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    public void givenArticleNumber_whenRequestingArticles_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articles/2/articleComments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
        // test
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    public void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articleComments"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
        // test
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    public void givenArticleCommentId_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // given

        // when
        mvc.perform(MockMvcRequestBuilders.get("/api/articleComments/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(MockMvcResultHandlers.print());
        // test
    }

    @DisplayName("[api] 회원 관련 API는 일체 제공하지 않는다.")
    @Test
    public void givenNothing_whenReqeustUserAccounts_thenThrowsException() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.post("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.put("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.patch("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
        mvc.perform(MockMvcRequestBuilders.delete("/api/userAccounts")).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
