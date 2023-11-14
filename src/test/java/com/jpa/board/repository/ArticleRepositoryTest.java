package com.jpa.board.repository;

import com.jpa.board.config.JpaConfig;
import com.jpa.board.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Test
    @DisplayName("select 테스트")
    public void givenTestData_whenSelecting_thenWorksFind() {
        // given

        // when
        List<Article> articles = articleRepository.findAll();
        // then
        assertThat(articles).isNotNull().hasSize(100);
    }


    @Test
    @DisplayName("insert 테스트")
    public void givenTestData_whenInserting_thenWorksFine() {
        // given
        long previousCount = articleRepository.count();
        Article article = Article.builder().title("new article").content("new content").hashtag("spring").build();

        // when
        Article savedArticle = articleRepository.save(article);

        // then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @Test
    @DisplayName("update 테스트")
    public void givenTestData_whenUpdating_thenWorksFine() {
        // given
        Article article = articleRepository.findById(1L).orElseThrow(() -> new NullPointerException());
        article.setHashtag("springboot");

        // when
        Article savedArticle = articleRepository.saveAndFlush(article);

        // then
        assertThat(article).isEqualTo(savedArticle);
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", "springboot");
    }

    @Test
    @DisplayName("delete 테스트")
    public void givenTestData_whenDeleting_thenWorksFine() {
        // given
        Article article = articleRepository.findById(1L).orElseThrow(() -> new NullPointerException());
        long previousCount = articleRepository.count();
        long previousCommnetCount = articleCommentRepository.count();
        int deletedCommentCount = article.getArticleComments().size();

        // when
        articleRepository.delete(article);

        // then
        assertThat(articleRepository.count()).isEqualTo(previousCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousCommnetCount - deletedCommentCount);
    }

}