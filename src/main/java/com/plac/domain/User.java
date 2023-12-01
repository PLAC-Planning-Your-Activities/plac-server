package com.plac.domain;

import com.plac.domain.mappedenum.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User extends AbstractTimeEntity {

    @Column(name = "password", nullable = false)
    String password;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "salt", nullable = true)
    @Type(type = "uuid-char")
    private UUID salt;

    @Column(name = "roles", nullable = false)
    private String roles;

    @Column(nullable = true)
    private String profileName;

    @Column(nullable = true)
    private String profileImageUrl;

    @Column(nullable = true)
    private String profileBirthday;

    @Column(nullable = true)
    private int age;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Comment("소셜 로그인시 갱신됨 (네이버, 카카오, 구글 중 하나)")
    @Column(name = "provider", nullable = true)
    private String provider;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt=" + salt +
                ", roles='" + roles + '\'' +
                ", profileName='" + profileName + '\'' +
                ", profileImagePath='" + profileImageUrl + '\'' +
                ", profileBirth='" + profileBirthday + '\'' +
                ", age=" + age +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createdAt=" + super.getCreatedAt() +
                ", updatedAt=" + super.getUpdatedAt() +
                ", provider='" + provider + '\'' +
                '}';
    }
}
