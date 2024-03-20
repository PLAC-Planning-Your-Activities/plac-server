package com.plac.domain.destination.repository;

import com.plac.domain.destination.entity.UserDestination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDestinationRepository extends JpaRepository<UserDestination, Long> {

    @Query("SELECT destination.name FROM UserDestination userDestination " +
            "JOIN userDestination.user user " +
            "JOIN userDestination.destination destination " +
            "WHERE user.ageRange = :ageRange AND user.gender = :gender " +
            "GROUP BY destination.name " +
            "ORDER BY COUNT(destination) DESC")
    List<String> findTop7DestinationsByAgeRangeAndGender(@Param("ageRange") int ageRange, @Param("gender") String gender, Pageable pageable);

    @Query("SELECT destination.name, COUNT(destination) as frequency " +
            "FROM UserDestination destinationMapping " +
            "JOIN destinationMapping.destination destination " +
            "GROUP BY destination " +
            "ORDER BY frequency DESC")
    List<String> findTop7Destinations(Pageable pageable);
}
