package com.plac.service.destination;

import com.plac.domain.destination.Destination;
import com.plac.domain.destination.DestinationStatistics;
import com.plac.domain.mappedenum.DestinationStatisticsStatus;
import com.plac.dto.response.destination.DestinationResDto;
import com.plac.dto.response.destination.TopDestinationResDto;
import com.plac.repository.destination.DestinationRepository;
import com.plac.repository.destination.DestinationStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DestinationService {
    private final DestinationStatisticsRepository destinationStatisticsRepository;

    private final DestinationRepository destinationRepository;

    //    @Cacheable(value = "topDestinations", key = "#count") // TODO 캐시해도 좋을듯
    public List<TopDestinationResDto> getTopDestinations(int count) {

        List<DestinationStatistics> topDestinations = destinationStatisticsRepository.findByStatusAndRankingLessThanEqual(DestinationStatisticsStatus.CURRENT, count);
        return topDestinations.stream()
                .map(statistics -> TopDestinationResDto.of(
                        statistics.getRanking(),
                        statistics.getDestinationId(),
                        statistics.getDestinationName(),
                        statistics.getDestinationThumbnailUrl())
                )
                .collect(Collectors.toList());
    }

    public List<DestinationResDto> getDestinations() {
        List<Destination> destinations = destinationRepository.findAll();

        return destinations.stream()
                .map(DestinationResDto::of)
                .collect(Collectors.toList());
    }

    public Destination getDestination(String id) {
        Optional<Destination> optionalDestination = destinationRepository.findById(id);
        if (optionalDestination.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 destinationId 입니다.");
        }
        return optionalDestination.get();
    }
}
