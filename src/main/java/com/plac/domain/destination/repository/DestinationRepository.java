package com.plac.domain.destination.repository;

import com.plac.domain.destination.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository <Destination, Long> {
}
