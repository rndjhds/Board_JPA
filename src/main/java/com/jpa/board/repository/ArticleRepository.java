package com.jpa.board.repository;

import com.jpa.board.domain.Article;
import com.jpa.board.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // Article 필드의 기본적인 검색기능을 지원해준다.
        QuerydslBinderCustomizer<QArticle> { // Article의 검색 기능을 수정 customize메서드를 이용해서 검색 기능을 수정

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.hashtag, root.createdAt, root.createdBy, root.content);
        //bindings.bind(root.title).first((path, value) -> path.likeIgnoreCase(value)); // like ${value}
        bindings.bind(root.title).first((path, value) -> path.containsIgnoreCase(value)); // like %${value}%
        bindings.bind(root.content).first((path, value) -> path.containsIgnoreCase(value)); // like %${value}%
        bindings.bind(root.hashtag).first((path, value) -> path.containsIgnoreCase(value)); // like %${value}%
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // like %${value}%
        bindings.bind(root.createdBy).first((path, value) -> path.containsIgnoreCase(value)); // like %${value}%
    }

    Page<Article> findByTitleContaining(String title, Pageable pageable);

    Page<Article> findByContentContaining(String content, Pageable pageable);

    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);

    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);

    Page<Article> findByHashtag(String hashtag, Pageable pageable);
}
