package com.plac.domain;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @Type(type = "uuid-char")
    public UUID id;

    private String refreshToken;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

}
