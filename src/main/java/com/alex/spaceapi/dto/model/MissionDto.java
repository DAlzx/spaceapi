package com.alex.spaceapi.dto.model;

import java.util.List;

/**
 * DTO pour créer ou mettre à jour une mission.
 * - name, launchDate, description : champs simples
 * - pilotUsername : identifiant du pilote (username)
 * - planetIds : liste d’IDs des planètes ciblées
 */
public record MissionDto(
        String name,
        String launchDate,          // format attendu YYYY-MM-DD
        String description,
        String pilotUsername,
        List<Long> planetIds
) {}
