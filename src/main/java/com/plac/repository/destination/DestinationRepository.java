package com.plac.repository.destination;

import com.plac.domain.destination.Destination;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends MongoRepository<Destination, String> {
}
