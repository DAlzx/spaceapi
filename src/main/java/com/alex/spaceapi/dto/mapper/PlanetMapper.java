package com.alex.spaceapi.dto.mapper;

import com.alex.spaceapi.dto.model.PlanetDto;
import com.alex.spaceapi.model.Planet;

/**
 * Permet de transformer un PlanetDto en Planet (entité JPA).
 */
public class PlanetMapper {

    public static Planet mapToPlanet(PlanetDto dto) {
        Planet planet = new Planet();
        planet.setName(dto.name());
        planet.setType(dto.type());
        planet.setDescription(dto.description());
        return planet;
    }
}
