package com.plac.domain.destination.service;

import com.plac.domain.destination.dto.request.SearchDestinationRequest;
import com.plac.domain.destination.entity.Destination;
import com.plac.domain.destination.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
