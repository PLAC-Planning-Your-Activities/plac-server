package com.plac.domain.destination.repository;

import com.plac.domain.destination.entity.UserSearchWords;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSearchWordsMappingRepository extends JpaRepository<UserSearchWords, Long> {

    @Query("SELECT searchWords.name, COUNT(searchWords) as frequency " +
            "FROM UserSearchWords searchWordsMapping " +
            "JOIN searchWordsMapping.user user " +
            "JOIN searchWordsMapping.searchWords searchWords " +
            "WHERE user.ageRange = :ageRange AND user.gender = :gender " +
            "GROUP BY searchWords.name " +
            "ORDER BY frequency DESC")
    List<String> findTop6SearchWordsByAgeRangeAndGender(@Param("ageRange") int ageRange, @Param("gender") String gender, Pageable pageable);

    @Query("SELECT searchWords.name, COUNT(searchWords) as frequency " +
            "FROM UserSearchWords searchWordsMapping " +
            "JOIN searchWordsMapping.searchWords searchWords " +
            "GROUP BY searchWords " +
            "ORDER BY frequency DESC")
    List<String> findTop6SearchWords(Pageable pageable);
}
