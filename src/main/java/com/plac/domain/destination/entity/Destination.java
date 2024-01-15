package com.plac.domain.destination.entity;

import com.plac.common.AbstractTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Destination extends AbstractTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Getter
    private String name;

    @Getter
    private int count;

    @Getter
    @Comment("검색용이면 1, 아니면 0")
    private boolean search;

    public Destination(String name, int count, boolean search) {
        this.name = name;
        this.count = count;
        this.search = search;
    }

    public void raiseCount(){
        this.count++;
    }
}
