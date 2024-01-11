package com.plac.domain.plan.entity;

import com.plac.common.AbstractTimeEntity;
import com.plac.domain.place.entity.Place;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PlanPlaceMapping extends AbstractTimeEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

}
