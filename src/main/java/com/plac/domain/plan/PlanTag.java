package com.plac.domain.plan;

import com.plac.domain.AbstractBaseDocument;
import com.plac.domain.mappedenum.PlanTagType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlanTag extends AbstractBaseDocument {
    @Id
    private String id;
    private PlanTagType type;
    private String name;

    public PlanTag(PlanTagType type, String name) {
        this.type = type;
        this.name = name;
    }
}
