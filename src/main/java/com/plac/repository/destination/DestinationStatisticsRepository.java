package com.plac.repository.destination;

import com.plac.domain.destination.DestinationStatistics;
import com.plac.domain.mappedenum.DestinationStatisticsStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationStatisticsRepository extends MongoRepository<DestinationStatistics, Long> {
//        , DestinationStatisticsRepositoryCustom {
    List<DestinationStatistics> findByStatusAndRankingLessThanEqual(DestinationStatisticsStatus status, int ranking);
}