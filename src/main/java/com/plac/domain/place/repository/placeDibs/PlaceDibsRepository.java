package com.plac.domain.place.repository.placeDibs;

import com.plac.domain.place.entity.PlaceDibs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceDibsRepository extends JpaRepository<PlaceDibs, Long>, PlaceDibsQueryRepository {
}
