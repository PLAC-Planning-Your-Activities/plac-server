package com.plac.domain.plan.entity;

import com.plac.common.AbstractTimeEntity;
import com.plac.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plan", orphanRemoval = true)
    List<PlanPlaceMapping> planPlaceMappings = new ArrayList<>();

    private String destinationName;

    @Column(columnDefinition = "tinyint(1)")
    private boolean open;

    public void changeOpenness(boolean value){
        this.open = value;
    }

    @Builder
    public Plan(String name, User user, boolean open, String destinationName) {
        this.name = name;
        this.user = user;
        this.open = open;
        this.destinationName = destinationName;
    }
}
