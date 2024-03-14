package com.plac.domain.user.entity;

import com.plac.common.AbstractTimeEntity;
import com.plac.common.mappedenum.UserStatus;
import com.plac.domain.user.dto.request.ChangeProfileRequest;
import lombok.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String username;

    String password;

    private String roles;

    private String profileName;

    private String profileImageUrl;

    private int age;

    private String gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    private String provider;

    private int ageRange;

    public void changeProfile(ChangeProfileRequest req) {
        this.profileName = req.getProfileName();
        this.profileImageUrl = req.getProfileImageUrl();
        this.gender = req.getGender();
        this.ageRange = req.getAgeGroup();
    }
}
