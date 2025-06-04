package com.alex.spaceapi.service;

import com.alex.spaceapi.model.Mission;

import java.util.List;
import java.util.Optional;

/**
 * Interface du service métier pour Mission (CRUD).
 */
public interface MissionService {

    Optional<Mission> get(Long missionId);

    Optional<Mission> add(Mission missionToAdd);

    Optional<Mission> update(Mission missionToUpdate);

    void delete(Mission mission);

    List<Mission> getAll();
}
