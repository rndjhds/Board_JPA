package com.jpa.board.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Getter
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@ToString(callSuper = true)
public class ArticleComment extends AuditingFields {

    @Builder
    public ArticleComment(String content, Article article, UserAccount userAccount) {
        this.content = content;
        this.article = article;
        this.userAccount = userAccount;
    }

    @Id
    @SequenceGenerator(name = "comment_jpa_seq", sequenceName = "comment_seq", initialValue = 101)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_jpa_seq")
    @Column(name = "comment_id")
    private Long id;
    @Setter
    @Column(nullable = false, length = 500)
    private String content; // 내용

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id")
    private Article article; // 게시글 (id)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;


    public void addArticle(Article article) {
        this.article = article;
        article.getArticleComments().add(this);
    }

    public void addUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment)) return false;
        ArticleComment that = (ArticleComment) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
