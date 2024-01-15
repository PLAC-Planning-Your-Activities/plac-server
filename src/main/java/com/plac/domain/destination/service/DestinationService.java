package com.plac.domain.destination.service;

import com.plac.domain.destination.repository.DestinationMappingRepository;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final DestinationMappingRepository destinationMappingRepository;
    private final UserRepository userRepository;

    public List<String> getTop7SearchWords(int filter) {
        Pageable topSeven = PageRequest.of(0, 7);

        if (filter == 1) {
            Long userId = SecurityContextHolderUtil.getUserId();
            User user = userRepository.findById(userId).get();

            List<String> result = destinationMappingRepository.findTop7DestinationsByAgeRangeAndGender(user.getAgeRange(), user.getGender(), topSeven);
            return result;
        }

        return destinationMappingRepository.findTop7Destinations(topSeven);
    }
}
