package com.alex.spaceapi.service;

import com.alex.spaceapi.model.Satellite;
import com.alex.spaceapi.repository.SatelliteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service Satellite.
 * Gère createdDate / updatedDate.
 */
@Service
public class SatelliteServiceImpl implements SatelliteService {

    private final SatelliteRepository satelliteRepository;

    public SatelliteServiceImpl(SatelliteRepository satelliteRepository) {
        this.satelliteRepository = satelliteRepository;
    }

    @Override
    public Optional<Satellite> get(Long satelliteId) {
        return satelliteRepository.findById(satelliteId);
    }

    @Override
    public Optional<Satellite> add(Satellite satelliteToAdd) {
        LocalDateTime now = LocalDateTime.now();
        satelliteToAdd.setCreatedDate(now);
        satelliteToAdd.setUpdatedDate(now);
        return Optional.of(satelliteRepository.save(satelliteToAdd));
    }

    @Override
    public Optional<Satellite> update(Satellite satelliteToUpdate) {
        satelliteToUpdate.setUpdatedDate(LocalDateTime.now());
        return Optional.of(satelliteRepository.save(satelliteToUpdate));
    }

    @Override
    public void delete(Satellite satellite) {
        satelliteRepository.delete(satellite);
    }

    @Override
    public List<Satellite> getAll() {
        return satelliteRepository.findAll();
    }
}
