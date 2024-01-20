package com.plac.domain.plan.entity;

import com.plac.common.AbstractTimeEntity;
import com.plac.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @OneToMany(mappedBy = "plan", orphanRemoval = true, fetch = FetchType.EAGER)
    List<PlanPlaceMapping> planPlaceMappings = new ArrayList<>();

    @OneToMany(mappedBy = "plan", orphanRemoval = true, fetch = FetchType.LAZY)
    List<PlanTagMapping> planTagMappings = new ArrayList<>();

    private String destinationName;

    @ColumnDefault("false")
    private boolean open;

    @Setter
    @ColumnDefault("false")
    private boolean isDeleted;

    private String imageUrl;

    public void changeOpenness(boolean value){
        this.open = value;
    }

    public void softDelete(){
        this.isDeleted = true;
    }

    @Builder
    public Plan(String name, User user, String destinationName, String imageUrl) {
        this.name = name;
        this.user = user;
        this.destinationName = destinationName;
        this.imageUrl = imageUrl;
    }
}
