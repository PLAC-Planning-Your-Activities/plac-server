package com.plac.domain.destination.repository;

import com.plac.domain.destination.entity.Destination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DestinationRepository extends JpaRepository <Destination, Long> {

    Optional<Destination> findByName(String name);

    @Query("SELECT destination FROM Destination destination ORDER BY destination.count DESC")
    List<Destination> findTop6ByCountDesc(Pageable pageable);
}
