package com.plac.domain.plan.entity;

import com.plac.common.AbstractTimeEntity;
import com.plac.domain.place.entity.PlaceDibs;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanDibs extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long planId;

    @NotNull
    private Long userId;

    @Builder
    public PlanDibs(Long planId, Long userId) {
        this.planId = planId;
        this.userId = userId;
    }

    public static PlanDibs create(Long planId, Long userId) {
        return PlanDibs.builder()
                .planId(planId)
                .userId(userId)
                .build();
    }
}
