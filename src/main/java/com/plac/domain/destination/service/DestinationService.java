package com.plac.domain.destination.service;

import com.plac.domain.destination.dto.request.SearchDestinationRequest;
import com.plac.domain.destination.dto.response.PopularWordsResponse;
import com.plac.domain.destination.entity.Destination;
import com.plac.domain.destination.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationRepository destinationRepository;

    @Transactional
    public void createSearchWords(SearchDestinationRequest destinationRequest) {
        List<String> words = destinationRequest.getWords();

        words.forEach(word -> destinationRepository.findByName(word)
                .ifPresentOrElse(
                        findDestination -> findDestination.raiseCount(),
                        () -> destinationRepository.save(new Destination(word, 0))
                )
        );
    }

    public List<PopularWordsResponse> getTop6SearchWords() {
        Pageable topSix = PageRequest.of(0, 6);
        List<Destination> destinations = destinationRepository.findTop6ByCountDesc(topSix);

        List<PopularWordsResponse> result = new ArrayList<>();

        for (Destination destination : destinations) {
            result.add(new PopularWordsResponse(destination.getName()));
        }
        return result;
    }
}
