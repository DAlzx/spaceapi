package com.alex.spaceapi.dto.mapper;

import com.alex.spaceapi.dto.model.MissionDto;
import com.alex.spaceapi.model.Mission;

import java.time.LocalDate;

/**
 * Mapper DTO → Mission (ne gère QUE les champs “plats” : name, launchDate, description).
 * Les relations (pilot, planets) seront fixées dans le contrôleur.
 */
public class MissionMapper {

    public static Mission mapToMission(MissionDto dto) {
        Mission mission = new Mission();
        mission.setName(dto.name());
        // Convertit la chaîne "YYYY-MM-DD" en LocalDate
        mission.setLaunchDate(LocalDate.parse(dto.launchDate()));
        mission.setDescription(dto.description());
        return mission;
    }
}
