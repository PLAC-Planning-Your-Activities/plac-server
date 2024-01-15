package com.plac.domain.destination.repository;

import com.plac.domain.destination.entity.DestinationMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DestinationMappingRepository extends JpaRepository<DestinationMapping, Long> {

    @Query("SELECT destination.name, COUNT(destination) as frequency " +
            "FROM DestinationMapping destinationMapping " +
            "JOIN destinationMapping.user user " +
            "JOIN destinationMapping.destination destination " +
            "WHERE user.ageRange = :ageRange AND user.gender = :gender " +
            "GROUP BY destination.name " +
            "ORDER BY frequency DESC")
    List<String> findTop7DestinationsByAgeRangeAndGender(@Param("ageRange") int ageRange, @Param("gender") String gender, Pageable pageable);

    @Query("SELECT destination.name, COUNT(destination) as frequency " +
            "FROM DestinationMapping destinationMapping " +
            "JOIN destinationMapping.destination destination " +
            "GROUP BY destination " +
            "ORDER BY frequency DESC")
    List<String> findTop7Destinations(Pageable pageable);
}
