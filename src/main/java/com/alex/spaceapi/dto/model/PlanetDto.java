package com.alex.spaceapi.dto.model;

/**
 * DTO pour créer ou mettre à jour une planète.
 * Contient uniquement les champs éditables.
 */
public record PlanetDto(
        String name,
        String type,
        String description
) {}
