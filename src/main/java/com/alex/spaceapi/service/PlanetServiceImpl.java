package com.alex.spaceapi.service;

import com.alex.spaceapi.model.Planet;
import com.alex.spaceapi.repository.PlanetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service Planet.
 * Gère la création des dates createdDate / updatedDate.
 */
@Service
public class PlanetServiceImpl implements PlanetService {

    private final PlanetRepository planetRepository;

    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public Optional<Planet> get(Long planetId) {
        return planetRepository.findById(planetId);
    }

    @Override
    public Optional<Planet> add(Planet planetToAdd) {
        LocalDateTime now = LocalDateTime.now();
        planetToAdd.setCreatedDate(now);
        planetToAdd.setUpdatedDate(now);
        return Optional.of(planetRepository.save(planetToAdd));
    }

    @Override
    public Optional<Planet> update(Planet planetToUpdate) {
        planetToUpdate.setUpdatedDate(LocalDateTime.now());
        return Optional.of(planetRepository.save(planetToUpdate));
    }

    @Override
    public void delete(Planet planet) {
        planetRepository.delete(planet);
    }

    @Override
    public List<Planet> getAll() {
        return planetRepository.findAll();
    }
}
