package com.plac.domain.destination.service;

import com.plac.domain.destination.repository.UserDestinationRepository;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.exception.common.DataNotFoundException;
import com.plac.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final UserDestinationRepository userDestinationRepository;
    private final UserRepository userRepository;

    public List<String> getTop7Destination(HttpServletRequest request) {
        User user = findUserFromAccessToken(request);

        Pageable topSeven = PageRequest.of(0, 7);
        List<String> result = userDestinationRepository.findTop7DestinationsByAgeRangeAndGender(user.getAgeRange(), user.getGender(), topSeven);

        return result;
    }

    private User findUserFromAccessToken(HttpServletRequest request) {
        String accessToken = JwtUtil.getAccessTokenFromCookie(request);
        Claims claims = JwtUtil.getClaimFromAccessToken(accessToken);

        String username = claims.get("username").toString();
        String provider = claims.get("provider").toString();

        User user = userRepository.findByUsernameAndProvider(username, provider)
                .orElseThrow(() -> new DataNotFoundException("user not found."));

        return user;
    }
}
