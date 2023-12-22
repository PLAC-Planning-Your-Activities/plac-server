package com.plac.domain.plan;

import com.plac.domain.AbstractBaseDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document
public class PlanLike extends AbstractBaseDocument {
    @Id
    private String id;
    private String planId;
    @Indexed
    private Long userId;

    public PlanLike(String planId, Long userId) {
        this.planId = planId;
        this.userId = userId;
    }
}
