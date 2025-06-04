package com.alex.spaceapi.repository;

import com.alex.spaceapi.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour l’entité Planet.
 */
@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
    // Pas de méthode custom pour l’instant
}
