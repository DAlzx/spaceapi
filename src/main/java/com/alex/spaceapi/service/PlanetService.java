package com.alex.spaceapi.service;

import com.alex.spaceapi.model.Planet;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service métier pour Planet (CRUD simple).
 */
public interface PlanetService {

    Optional<Planet> get(Long planetId);

    Optional<Planet> add(Planet planetToAdd);

    Optional<Planet> update(Planet planetToUpdate);

    void delete(Planet planet);

    List<Planet> getAll();
}
