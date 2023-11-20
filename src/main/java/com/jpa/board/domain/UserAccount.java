package com.jpa.board.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount extends AuditingFields{

    @Id
    @SequenceGenerator(name = "account_id_jpa_seq", sequenceName = "account_id_seq", initialValue = 2)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_jpa_seq")
    private Long id;

    @Setter @Column(nullable = false, length = 50) private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;

    @Builder
    public UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount)) return false;
        UserAccount that = (UserAccount) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
