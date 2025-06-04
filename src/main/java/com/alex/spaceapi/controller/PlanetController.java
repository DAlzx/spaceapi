package com.alex.spaceapi.controller;

import com.alex.spaceapi.dto.mapper.PlanetMapper;
import com.alex.spaceapi.dto.model.PlanetDto;
import com.alex.spaceapi.model.Planet;
import com.alex.spaceapi.service.PlanetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Planet services", description = "CRUD sur les planètes")
@RestController
@RequestMapping("/planets")
public class PlanetController {

    private final PlanetService planetService;

    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @Operation(summary = "Récupérer une planète par son ID")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/{planetId}")
    public Planet get(@PathVariable Long planetId) {
        return planetService.get(planetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Créer une nouvelle planète")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public Planet add(@RequestBody PlanetDto planetDto) {
        // Mapper DTO → entité
        Planet p = PlanetMapper.mapToPlanet(planetDto);
        return planetService.add(p)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Operation(summary = "Mettre à jour une planète existante")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{planetId}")
    public Planet update(@PathVariable Long planetId, @RequestBody PlanetDto planetDto) {
        Planet existing = planetService.get(planetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Planet toUpdate = PlanetMapper.mapToPlanet(planetDto);
        toUpdate.setId(existing.getId());
        toUpdate.setCreatedDate(existing.getCreatedDate());

        return planetService.update(toUpdate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Operation(summary = "Supprimer une planète existante")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{planetId}")
    public ResponseEntity<Void> delete(@PathVariable Long planetId) {
        Planet existing = planetService.get(planetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        planetService.delete(existing);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer la liste de toutes les planètes")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping
    public List<Planet> getAll() {
        return planetService.getAll();
    }
}
