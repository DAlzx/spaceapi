package com.alex.spaceapi.service;

import com.alex.spaceapi.model.Observation;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service métier pour Observation (CRUD).
 */
public interface ObservationService {

    Optional<Observation> get(Long observationId);

    Optional<Observation> add(Observation observationToAdd);

    Optional<Observation> update(Observation observationToUpdate);

    void delete(Observation observation);

    List<Observation> getAll();
}
