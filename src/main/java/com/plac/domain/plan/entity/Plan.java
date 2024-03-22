package com.plac.domain.plan.entity;

import com.plac.common.AbstractTimeEntity;
import com.plac.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Plan extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "plan", orphanRemoval = true, fetch = FetchType.LAZY)
    List<PlanPlaceMapping> planPlaceMappings = new ArrayList<>();

    @OneToMany(mappedBy = "plan", orphanRemoval = true, fetch = FetchType.LAZY)
    List<PlanTagMapping> planTagMappings = new ArrayList<>();

    @NotNull
    private String destinationName;

    @Column(columnDefinition = "tinyint(1)")
    private boolean open;

    public void changeOpenness(boolean value){
        this.open = value;
    }

    @Builder
    public Plan(String name, User user, String destinationName) {
        this.name = name;
        this.user = user;
        this.destinationName = destinationName;
        this.open = false;
    }
}
