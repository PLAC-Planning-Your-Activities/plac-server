package com.plac.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, columnDefinition = "varchar(255)")
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "text")
    String password;

    @Column(name = "salt", nullable = true)
    @Type(type = "uuid-char")
    private UUID salt;

    @Column(name = "roles", nullable = true, columnDefinition = "varchar(36)")
    private String roles;

    @Column(name = "profile_name", nullable = true, columnDefinition = "varchar(36)")
    private String profileName;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true, columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    @Comment("소셜 로그인시 갱신됨 (네이버, 카카오, 구글 중 하나)")
    @Column(name = "provider", nullable = true, columnDefinition = "varchar(36)")
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

    public String getUsername(){
        return this.username;
    }

}
