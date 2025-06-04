package com.alex.spaceapi.service;

import com.alex.spaceapi.model.Satellite;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service métier pour Satellite (CRUD).
 */
public interface SatelliteService {

    Optional<Satellite> get(Long satelliteId);

    Optional<Satellite> add(Satellite satelliteToAdd);

    Optional<Satellite> update(Satellite satelliteToUpdate);

    void delete(Satellite satellite);

    List<Satellite> getAll();
}
