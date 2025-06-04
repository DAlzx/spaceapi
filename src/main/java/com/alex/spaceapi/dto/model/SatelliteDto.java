package com.alex.spaceapi.dto.model;

/**
 * DTO pour créer ou mettre à jour un satellite :
 * - name, type : champs simples
 * - planetId      : ID de la planète associée
 */
public record SatelliteDto(
        String name,
        String type,
        Long planetId
) {}
