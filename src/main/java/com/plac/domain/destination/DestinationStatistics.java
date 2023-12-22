package com.plac.domain.destination;
;
import com.plac.domain.AbstractBaseDocument;
import com.plac.domain.mappedenum.DestinationStatisticsStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@CompoundIndex(def = "{'status': 1, 'ranking': 1}", name = "status_ranking_idx")
@Document
public class DestinationStatistics extends AbstractBaseDocument {
    @Id
    private String id;

    private int ranking;

    private String destinationId;

    private String destinationName;

    private String destinationThumbnailUrl;

    private int count;

    @Builder.Default
    private DestinationStatisticsStatus status = DestinationStatisticsStatus.CURRENT;
}
