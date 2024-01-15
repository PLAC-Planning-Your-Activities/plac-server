package com.plac.domain.plan.entity;

import com.plac.common.AbstractTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanTagMapping extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private Long planTagId;

    @Builder
    public PlanTagMapping(Plan plan, Long planTagId) {
        this.plan = plan;
        this.planTagId = planTagId;
    }
}
