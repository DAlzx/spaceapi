package com.alex.spaceapi.repository;

import com.alex.spaceapi.model.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour l’entité Satellite.
 */
@Repository
public interface SatelliteRepository extends JpaRepository<Satellite, Long> {
    // Pas de méthode custom pour l’instant
}
