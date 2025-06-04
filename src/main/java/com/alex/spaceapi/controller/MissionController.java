package com.alex.spaceapi.controller;

import com.alex.spaceapi.dto.mapper.MissionMapper;
import com.alex.spaceapi.dto.model.MissionDto;
import com.alex.spaceapi.model.Mission;
import com.alex.spaceapi.model.Planet;
import com.alex.spaceapi.model.User;
import com.alex.spaceapi.repository.PlanetRepository;
import com.alex.spaceapi.repository.UserRepository;
import com.alex.spaceapi.service.MissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contrôleur REST pour l’entité Mission.
 * - GET /missions/{id}
 * - POST /missions
 * - PUT /missions/{id}
 * - DELETE /missions/{id}
 * - GET /missions
 */
@Tag(name = "Mission services", description = "CRUD sur les missions")
@RestController
@RequestMapping("/missions")
public class MissionController {

    private final MissionService missionService;
    private final UserRepository userRepository;
    private final PlanetRepository planetRepository;

    public MissionController(MissionService missionService,
                             UserRepository userRepository,
                             PlanetRepository planetRepository) {
        this.missionService = missionService;
        this.userRepository = userRepository;
        this.planetRepository = planetRepository;
    }

    @Operation(summary = "Récupérer une mission par ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{missionId}")
    public Mission get(@PathVariable Long missionId) {
        return missionService.get(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Créer une nouvelle mission")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Mission> add(@RequestBody MissionDto missionDto) {
        // 1. Mapper DTO → entité partielle (name, launchDate, description)
        Mission mission = MissionMapper.mapToMission(missionDto);

        // 2. Récupérer le pilote (User) depuis le username
        User pilot = userRepository.findByUsername(missionDto.pilotUsername());
        if (pilot == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pilot not found: " + missionDto.pilotUsername());
        }
        mission.setPilot(pilot);

        // 3. Récupérer les planètes par leurs IDs
        Set<Planet> planets = new HashSet<>();
        if (missionDto.planetIds() != null) {
            for (Long pid : missionDto.planetIds()) {
                Planet p = planetRepository.findById(pid)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Planet not found: " + pid));
                planets.add(p);
            }
        }
        mission.setPlanets(planets);

        // 4. Sauvegarder via le service
        Mission saved = missionService.add(mission)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        // 5. Retourner 201 Created avec l’objet en JSON
        return ResponseEntity
                .created(URI.create("/missions/" + saved.getId()))
                .body(saved);
    }

    @Operation(summary = "Mettre à jour une mission existante")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{missionId}")
    public Mission update(@PathVariable Long missionId, @RequestBody MissionDto missionDto) {
        Mission existing = missionService.get(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 1. Mapper les champs “plats”
        Mission toUpdate = MissionMapper.mapToMission(missionDto);

        // 2. Conserver l’ID + createdDate
        toUpdate.setId(existing.getId());
        toUpdate.setCreatedDate(existing.getCreatedDate());

        // 3. Mettre à jour le pilote
        User pilot = userRepository.findByUsername(missionDto.pilotUsername());
        if (pilot == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pilot not found: " + missionDto.pilotUsername());
        }
        toUpdate.setPilot(pilot);

        // 4. Mettre à jour la liste des planètes
        Set<Planet> planets = new HashSet<>();
        if (missionDto.planetIds() != null) {
            for (Long pid : missionDto.planetIds()) {
                Planet p = planetRepository.findById(pid)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Planet not found: " + pid));
                planets.add(p);
            }
        }
        toUpdate.setPlanets(planets);

        // 5. Sauvegarder
        return missionService.update(toUpdate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Operation(summary = "Supprimer une mission existante")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{missionId}")
    public ResponseEntity<Void> delete(@PathVariable Long missionId) {
        Mission existing = missionService.get(missionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        missionService.delete(existing);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer la liste de toutes les missions")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public List<Mission> getAll() {
        return missionService.getAll();
    }
}
