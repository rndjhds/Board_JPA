package com.jpa.board.repository;

import com.jpa.board.domain.ArticleComment;
import com.jpa.board.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.createdAt, root.createdBy, root.content);
        bindings.bind(root.content).first((path, value) -> path.containsIgnoreCase(value)); // like %${value}%
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // like %${value}%
        bindings.bind(root.createdBy).first((path, value) -> path.containsIgnoreCase(value)); // like %${value}%
    }

    List<ArticleComment> findByArticle_Id(Long articleId);
}
