package com.jpa.board.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@DynamicUpdate
@Getter
@ToString(callSuper = true)
public class Article extends AuditingFields{
    @Id
    @SequenceGenerator(name = "article_jpa_seq", sequenceName = "article_seq", initialValue = 101)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_jpa_seq")
    @Column(name = "article_id")
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 내용

    @Setter
    private String hashtag; // 해시태그

    @ToString.Exclude
    @OrderBy(value = "createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final List<ArticleComment> articleComments = new LinkedList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id")
    private UserAccount userAccount;

    public void addUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Builder
    public Article(UserAccount userAccount, String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return getId() != null && getId().equals(article.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
