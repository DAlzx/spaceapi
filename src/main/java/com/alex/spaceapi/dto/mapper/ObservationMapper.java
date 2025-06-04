package com.alex.spaceapi.dto.mapper;

import com.alex.spaceapi.dto.model.ObservationDto;
import com.alex.spaceapi.model.Observation;

import java.time.LocalDateTime;

/**
 * Mapper pour copier les champs “plats” d’ObservationDto dans Observation.
 * - timestamp est fixé à maintenant dans le contrôleur.
 * - createdDate / updatedDate sont gérés dans le service.
 * - Les relations (observer, planet) sont résolues dans le contrôleur.
 */
public class ObservationMapper {

    public static Observation mapToObservation(ObservationDto dto) {
        Observation obs = new Observation();
        obs.setType(dto.type());
        obs.setResult(dto.result());
        // Le timestamp de l’observation est fixé dans le contrôleur.
        return obs;
    }
}
