package com.plac.domain.destination.repository;

import com.plac.domain.destination.entity.UserSearchWords;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSearchWordsMappingRepository extends JpaRepository<UserSearchWords, Long> {

    @Query("SELECT searchWords.name " +
            "FROM UserSearchWords searchWordsMapping " +
            "JOIN searchWordsMapping.user user " +
            "JOIN searchWordsMapping.searchWords searchWords " +
            "WHERE user.ageGroup = :ageGroup AND user.gender = :gender " +
            "GROUP BY searchWords.name " +
            "ORDER BY COUNT(searchWords) DESC")
    List<String> findTop6SearchWordsByAgeGroupAndGender(@Param("ageGroup") int ageGroup, @Param("gender") String gender, Pageable pageable);

    @Query("SELECT searchWords.name " +
            "FROM UserSearchWords searchWordsMapping " +
            "JOIN searchWordsMapping.searchWords searchWords " +
            "GROUP BY searchWords " +
            "ORDER BY COUNT(searchWords)")
    List<String> findTop6SearchWords(Pageable pageable);
}
