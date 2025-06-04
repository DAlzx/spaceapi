package com.alex.spaceapi.service;

import com.alex.spaceapi.model.Observation;
import com.alex.spaceapi.repository.ObservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service Observation.
 * Gère createdDate / updatedDate.
 */
@Service
public class ObservationServiceImpl implements ObservationService {

    private final ObservationRepository observationRepository;

    public ObservationServiceImpl(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @Override
    public Optional<Observation> get(Long observationId) {
        return observationRepository.findById(observationId);
    }

    @Override
    public Optional<Observation> add(Observation observationToAdd) {
        LocalDateTime now = LocalDateTime.now();
        observationToAdd.setCreatedDate(now);
        observationToAdd.setUpdatedDate(now);
        return Optional.of(observationRepository.save(observationToAdd));
    }

    @Override
    public Optional<Observation> update(Observation observationToUpdate) {
        observationToUpdate.setUpdatedDate(LocalDateTime.now());
        return Optional.of(observationRepository.save(observationToUpdate));
    }

    @Override
    public void delete(Observation observation) {
        observationRepository.delete(observation);
    }

    @Override
    public List<Observation> getAll() {
        return observationRepository.findAll();
    }
}
