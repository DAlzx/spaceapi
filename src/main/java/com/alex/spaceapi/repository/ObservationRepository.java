package com.alex.spaceapi.repository;

import com.alex.spaceapi.model.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour l’entité Observation.
 */
@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {
    // Aucune méthode custom pour l’instant
}
