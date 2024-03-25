package com.plac.domain.destination.service;

import com.plac.domain.destination.repository.UserDestinationRepository;
import com.plac.domain.user.entity.User;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final UserDestinationRepository userDestinationRepository;

    public List<String> getTop7Destination() {
        User user = SecurityContextHolderUtil.getUser();

        Pageable topSeven = PageRequest.of(0, 7);
        List<String> result = userDestinationRepository.findTop7DestinationsByAgeRangeAndGender(user.getAgeRange(), user.getGender(), topSeven);

        return result;
    }
}
