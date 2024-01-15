package com.plac.domain.destination.repository;

import com.plac.domain.destination.entity.SearchWords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchWordsRepository extends JpaRepository<SearchWords, Long> {
    Optional<SearchWords> findByName(String name);

}
