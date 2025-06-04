package com.alex.spaceapi.dto.model;

/**
 * DTO pour créer ou mettre à jour une observation.
 * - type : type de l’observation
 * - result : description ou URL
 * - observerUsername : username de l’observateur
 * - planetId : ID de la planète observée
 */
public record ObservationDto(
        String type,
        String result,
        String observerUsername,
        Long planetId
) {}
