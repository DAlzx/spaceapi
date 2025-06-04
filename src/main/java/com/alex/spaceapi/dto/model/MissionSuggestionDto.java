package com.alex.spaceapi.dto.model;

/**
 * DTO pour demander à l'IA une suggestion de mission spatiale.
 *
 * @param targetPlanet le nom de la planète (ou corps céleste) ciblé(e) par la mission
 * @param missionType  type de mission souhaité ("exploration", "échantillonnage", "cartographie", etc.)
 * @param durationDays durée approximative de la mission en jours
 * @param objectives   liste d’objectifs secondaires (ex : "étudier les anneaux", "analyser l’atmosphère")
 */
public record MissionSuggestionDto(
        String targetPlanet,
        String missionType,
        Integer durationDays,
        String objectives
) {}
