package com.alex.spaceapi.dto.model;

/**
 * DTO pour demander à l'IA des conseils d'observation astronomique.
 *
 * @param target        nom de la planète ou de l'objet céleste (ex: "Jupiter", "Andromède")
 * @param instrument    type d'instrument utilisé ("télescope optique", "radio-télescope", "caméra infrarouge")
 * @param observationDate date souhaitée pour l’observation au format ISO (YYYY-MM-DD)
 * @param location       lieu d'observation (pays ou ville) pour estimer la météo et la visibilité
 */
public record ObservationTipDto(
        String target,
        String instrument,
        String observationDate,
        String location
) {}
