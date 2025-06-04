package com.alex.spaceapi.service;

import com.alex.spaceapi.model.Mission;
import com.alex.spaceapi.repository.MissionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service Mission.
 * Gère createdDate / updatedDate.
 */
@Service
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;

    public MissionServiceImpl(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Override
    public Optional<Mission> get(Long missionId) {
        return missionRepository.findById(missionId);
    }

    @Override
    public Optional<Mission> add(Mission missionToAdd) {
        LocalDateTime now = LocalDateTime.now();
        missionToAdd.setCreatedDate(now);
        missionToAdd.setUpdatedDate(now);
        return Optional.of(missionRepository.save(missionToAdd));
    }

    @Override
    public Optional<Mission> update(Mission missionToUpdate) {
        missionToUpdate.setUpdatedDate(LocalDateTime.now());
        return Optional.of(missionRepository.save(missionToUpdate));
    }

    @Override
    public void delete(Mission mission) {
        missionRepository.delete(mission);
    }

    @Override
    public List<Mission> getAll() {
        return missionRepository.findAll();
    }
}
