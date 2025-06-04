package com.alex.spaceapi.controller;

import com.alex.spaceapi.dto.mapper.SatelliteMapper;
import com.alex.spaceapi.dto.model.SatelliteDto;
import com.alex.spaceapi.model.Planet;
import com.alex.spaceapi.model.Satellite;
import com.alex.spaceapi.repository.PlanetRepository;
import com.alex.spaceapi.service.SatelliteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Contrôleur REST pour l’entité Satellite.
 * - GET /satellites/{id}
 * - GET /satellites
 * - POST /satellites
 * - PUT /satellites/{id}
 * - DELETE /satellites/{id}
 */
@Tag(name = "Satellite services", description = "CRUD sur les satellites")
@RestController
@RequestMapping("/satellites")
public class SatelliteController {

    private final SatelliteService satelliteService;
    private final PlanetRepository planetRepository;

    public SatelliteController(SatelliteService satelliteService,
                               PlanetRepository planetRepository) {
        this.satelliteService = satelliteService;
        this.planetRepository = planetRepository;
    }

    @Operation(summary = "Récupérer un satellite par ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{satelliteId}")
    public Satellite get(@PathVariable Long satelliteId) {
        return satelliteService.get(satelliteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Récupérer la liste de tous les satellites")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public List<Satellite> getAll() {
        return satelliteService.getAll();
    }

    @Operation(summary = "Créer un nouveau satellite")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public Satellite add(@RequestBody SatelliteDto satelliteDto) {
        // 1. Mapper DTO → entité partiel (name, type)
        Satellite sat = SatelliteMapper.mapToSatellite(satelliteDto);

        // 2. Récupérer la planète (planetId)
        Planet planet = planetRepository.findById(satelliteDto.planetId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Planet not found: " + satelliteDto.planetId()
                ));
        sat.setPlanet(planet);

        // 3. Sauvegarder
        return satelliteService.add(sat)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Operation(summary = "Mettre à jour un satellite existant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{satelliteId}")
    public Satellite update(@PathVariable Long satelliteId,
                             @RequestBody SatelliteDto satelliteDto) {
        // 1. Vérifier que le satellite existe
        Satellite existing = satelliteService.get(satelliteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 2. Mapper DTO → entité partiel (name, type)
        Satellite toUpdate = SatelliteMapper.mapToSatellite(satelliteDto);
        toUpdate.setId(existing.getId());
        toUpdate.setCreatedDate(existing.getCreatedDate());

        // 3. Récupérer la planète associée
        Planet planet = planetRepository.findById(satelliteDto.planetId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Planet not found: " + satelliteDto.planetId()
                ));
        toUpdate.setPlanet(planet);

        // 4. Sauvegarder
        return satelliteService.update(toUpdate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Operation(summary = "Supprimer un satellite existant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{satelliteId}")
    public ResponseEntity<Void> delete(@PathVariable Long satelliteId) {
        Satellite existing = satelliteService.get(satelliteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        satelliteService.delete(existing);
        return ResponseEntity.noContent().build();
    }
}
