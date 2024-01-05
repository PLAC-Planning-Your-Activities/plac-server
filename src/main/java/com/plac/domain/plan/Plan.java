package com.plac.domain.plan;

import com.plac.domain.AbstractBaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document
@CompoundIndex(def = "{'tags.type': 1, 'tags.name': 1}", name = "tags_type_name_idx")
@CompoundIndex(def = "{'createdAt': -1}", name = "created_at_desc_idx")
public class Plan extends AbstractBaseDocument {
    @Id
    private String id;

    private Long userId;

    @Indexed
    private String userNickname;

    private String name;

    private String userProfileImageUrl;

    private String thumbnailImageUrl;

    @Indexed
    private String destinationId;

    private String destinationName;

    private List<Long> placeIdList;

    private boolean openStatus;

    private Long parentPlanId;

    private List<PlanTag> tags;

    @Builder.Default
    private int likeCount = 0;

    @Builder.Default
    private int scrapCount = 0;
}
