package com.plac.domain.plan.entity;

import com.plac.common.AbstractTimeEntity;
import com.plac.domain.user.entity.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends AbstractTimeEntity {

    @Id
    private Long id;

    private String name;

    private String thumbnailImage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    private boolean open;

    private String destinationName;

    @Builder
    public Plan(String name, String thumbnailImage, User user, boolean open, String destinationName) {
        this.name = name;
        this.thumbnailImage = thumbnailImage;
        this.user = user;
        this.open = open;
        this.destinationName = destinationName;
    }
}
