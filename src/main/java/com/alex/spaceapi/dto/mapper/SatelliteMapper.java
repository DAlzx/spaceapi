package com.alex.spaceapi.dto.mapper;

import com.alex.spaceapi.dto.model.SatelliteDto;
import com.alex.spaceapi.model.Satellite;

/**
 * Mapper pour copier les champs “plats” d’un SatelliteDto dans un Satellite.
 * Les relations seront fixées dans le contrôleur (résolution de planetId → Planet).
 */
public class SatelliteMapper {

    public static Satellite mapToSatellite(SatelliteDto dto) {
        Satellite sat = new Satellite();
        sat.setName(dto.name());
        sat.setType(dto.type());
        return sat;
    }
}
