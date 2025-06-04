package com.alex.spaceapi.controller;

import com.alex.spaceapi.dto.mapper.ObservationMapper;
import com.alex.spaceapi.dto.model.ObservationDto;
import com.alex.spaceapi.model.Observation;
import com.alex.spaceapi.model.Planet;
import com.alex.spaceapi.model.User;
import com.alex.spaceapi.repository.PlanetRepository;
import com.alex.spaceapi.repository.UserRepository;
import com.alex.spaceapi.service.ObservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST pour l’entité Observation.
 * - GET /observations/{id}
 * - GET /observations
 * - POST /observations
 * - PUT /observations/{id}
 * - DELETE /observations/{id}
 */
@Tag(name = "Observation services", description = "CRUD sur les observations")
@RestController
@RequestMapping("/observations")
public class ObservationController {

    private final ObservationService observationService;
    private final UserRepository userRepository;
    private final PlanetRepository planetRepository;

    public ObservationController(ObservationService observationService,
                                 UserRepository userRepository,
                                 PlanetRepository planetRepository) {
        this.observationService = observationService;
        this.userRepository = userRepository;
        this.planetRepository = planetRepository;
    }

    @Operation(summary = "Récupérer une observation par ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{observationId}")
    public Observation get(@PathVariable Long observationId) {
        return observationService.get(observationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Récupérer la liste de toutes les observations")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public List<Observation> getAll() {
        return observationService.getAll();
    }

    @Operation(summary = "Créer une nouvelle observation")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Observation> add(@RequestBody ObservationDto observationDto) {
        // 1. Mapper DTO → entité partielle (type, result)
        Observation obs = ObservationMapper.mapToObservation(observationDto);

        // 2. Fixer le timestamp à maintenant
        obs.setTimestamp(LocalDateTime.now());

        // 3. Récupérer l’observateur (User) via observerUsername
        User observer = userRepository.findByUsername(observationDto.observerUsername());
        if (observer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Observer not found: " + observationDto.observerUsername());
        }
        obs.setObserver(observer);

        // 4. Récupérer la planète via planetId
        Planet planet = planetRepository.findById(observationDto.planetId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Planet not found: " + observationDto.planetId()
                ));
        obs.setPlanet(planet);

        // 5. Sauvegarder
        Observation saved = observationService.add(obs)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));

        // 6. Retourner 201 Created avec l’objet en JSON
        return ResponseEntity
                .created(URI.create("/observations/" + saved.getId()))
                .body(saved);
    }

    @Operation(summary = "Mettre à jour une observation existante")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PutMapping("/{observationId}")
    public Observation update(@PathVariable Long observationId,
                              @RequestBody ObservationDto observationDto) {
        Observation existing = observationService.get(observationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 1. Mapper DTO → entité partielle (type, result)
        Observation toUpdate = ObservationMapper.mapToObservation(observationDto);
        toUpdate.setId(existing.getId());
        toUpdate.setCreatedDate(existing.getCreatedDate());
        // Conserver le timestamp d’origine ou le remplacer ? Ici on garde l’ancien timestamp.

        toUpdate.setTimestamp(existing.getTimestamp());

        // 2. Récupérer l’observateur
        User observer = userRepository.findByUsername(observationDto.observerUsername());
        if (observer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Observer not found: " + observationDto.observerUsername());
        }
        toUpdate.setObserver(observer);

        // 3. Récupérer la planète
        Planet planet = planetRepository.findById(observationDto.planetId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Planet not found: " + observationDto.planetId()
                ));
        toUpdate.setPlanet(planet);

        // 4. Sauvegarder
        return observationService.update(toUpdate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Operation(summary = "Supprimer une observation existante")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{observationId}")
    public ResponseEntity<Void> delete(@PathVariable Long observationId) {
        Observation existing = observationService.get(observationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        observationService.delete(existing);
        return ResponseEntity.noContent().build();
    }
}
