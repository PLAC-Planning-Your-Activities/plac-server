package com.plac.domain.plan.entity;

import com.plac.common.AbstractTimeEntity;
import com.plac.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritePlan extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Setter
    @Column(columnDefinition = "tinyint(1)")
    private boolean favorite;

    @Builder
    public FavoritePlan(User user, Plan plan, boolean favorite) {
        this.user = user;
        this.plan = plan;
        this.favorite = favorite;
    }
}
