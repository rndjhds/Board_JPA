package com.jpa.board.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
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
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@Getter
public class Article {
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

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt; // 생성일시

    @CreatedBy
    @Column(updatable = false, nullable = false, length = 100)
    private String createdBy; // 생성자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자

    @OrderBy(value = "id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final List<ArticleComment> articleComments = new LinkedList<>();

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Article{");
        sb.append("id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", content='").append(content).append('\'');
        sb.append(", hashtag='").append(hashtag).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", createdBy='").append(createdBy).append('\'');
        sb.append(", modifiedAt=").append(modifiedAt);
        sb.append(", modifiedBy='").append(modifiedBy).append('\'');
        sb.append('}');
        return sb.toString();
    }


    @Builder
    public Article(String title, String content, String hashtag) {
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
