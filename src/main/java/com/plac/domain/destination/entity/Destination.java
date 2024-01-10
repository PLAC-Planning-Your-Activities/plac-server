package com.plac.domain.destination.entity;

import com.plac.common.AbstractTimeEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Destination extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String imageUrl;

    private int count;

    public Destination(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public void raiseCount(){
        this.count++;
    }
}
