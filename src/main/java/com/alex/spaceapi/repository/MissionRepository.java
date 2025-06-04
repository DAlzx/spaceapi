package com.alex.spaceapi.repository;

import com.alex.spaceapi.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour l’entité Mission.
 */
@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    // Aucune méthode custom pour l’instant
}
