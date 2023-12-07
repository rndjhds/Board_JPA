package com.jpa.board.controller;

import com.jpa.board.domain.type.SearchType;
import com.jpa.board.dto.response.ArticleResponse;
import com.jpa.board.dto.response.ArticleWithCommentResponse;
import com.jpa.board.service.ArticleService;
import com.jpa.board.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/articles")
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;

    private final PaginationService paginationService;

    @GetMapping
    public String articles(@RequestParam(required = false) SearchType searchType
            , @RequestParam(required = false) String searchValue
            , @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            , Model model) {
        Page<ArticleResponse> articleResponses = articleService.searchPagingArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        model.addAttribute("articles", articleResponses);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articleResponses.getTotalPages());
        model.addAttribute("paginationBarNumbers", barNumbers);
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String articles(@PathVariable Long articleId, Model model) {
        ArticleWithCommentResponse article = ArticleWithCommentResponse.from(articleService.getArticle(articleId));
        model.addAttribute("article", article);
        model.addAttribute("articleComments", article.getArticleCommentResponses());
        return "articles/detail";
    }


}
