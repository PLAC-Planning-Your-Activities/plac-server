package com.plac.domain.destination.service;

import com.plac.domain.destination.dto.request.CreateSearchWordsRequest;
import com.plac.domain.destination.entity.SearchWords;
import com.plac.domain.destination.entity.UserSearchWords;
import com.plac.domain.destination.repository.SearchWordsRepository;
import com.plac.domain.destination.repository.UserSearchWordsMappingRepository;
import com.plac.domain.user.entity.User;
import com.plac.domain.user.repository.UserRepository;
import com.plac.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchWordsService {

    private final SearchWordsRepository searchWordsRepository;
    private final UserSearchWordsMappingRepository userSearchWordsMappingRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createSearchWords(CreateSearchWordsRequest userRequest) {
        Long userId = SecurityContextHolderUtil.getUserId();
        User user = userRepository.findById(userId).get();

        List<String> words = userRequest.getWords();

        for (String word : words) {
            SearchWords searchWords;
            Optional<SearchWords> searchWordsOpt = searchWordsRepository.findByName(word);

            if (!searchWordsOpt.isPresent()) {
                searchWords = searchWordsRepository.save(new SearchWords(word));
            }
            else searchWords = searchWordsOpt.get();

            UserSearchWords userSearchWords = UserSearchWords.builder()
                    .user(user)
                    .searchWords(searchWords)
                    .build();

            userSearchWordsMappingRepository.save(userSearchWords);
        }
    }

    public List<String> getTop6SearchWords(int filter) {
        Long userId = SecurityContextHolderUtil.getUserId();
        User user = userRepository.findById(userId).get();

        Pageable topSix = PageRequest.of(0, 6);

        if(filter == 1) {
            List<String> searchWordsNameList = userSearchWordsMappingRepository.findTop6SearchWordsByAgeRangeAndGender(
                    user.getAgeRange(), user.getGender(), topSix);
            return searchWordsNameList;
        }

        List<String> top6SearchWords = userSearchWordsMappingRepository.findTop6SearchWords(topSix);
        return top6SearchWords;
    }

}
