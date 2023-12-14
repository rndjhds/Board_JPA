package com.jpa.board.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jpa.board.domain.QArticle.article;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findAllDistinctHashtags() {
        return queryFactory
                .select(article.hashtag)
                .distinct()
                .from(article)
                .where(article.hashtag.isNotNull()).fetch();
    }
}
