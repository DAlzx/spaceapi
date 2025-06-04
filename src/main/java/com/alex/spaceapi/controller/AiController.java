package com.alex.spaceapi.controller;

import com.alex.spaceapi.dto.model.MissionSuggestionDto;
import com.alex.spaceapi.dto.model.ObservationTipDto;
import com.alex.spaceapi.service.MistralAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour exposer les endpoints AI de SpaceAPI :
 * - Suggestions de mission spatiale
 * - Conseils d'observation astronomique
 * - Chat libre
 */
@Tag(name = "AI services", description = "Fonctions IA pour suggestions de mission, conseils d'observation et chat")
@RestController
@RequestMapping("/ai")
public class AiController {

    private final MistralAiService mistralAiService;

    public AiController(MistralAiService mistralAiService) {
        this.mistralAiService = mistralAiService;
    }

    @Operation(
        summary = "Obtenir une suggestion de mission spatiale depuis l'IA",
        description = "Envoie des critères (planète, type, durée, objectifs) et reçoit un plan de mission détaillé"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plan de mission généré par l'IA",
                     content = { @Content(mediaType = "application/json",
                                           schema = @Schema(implementation = String.class)) }),
        @ApiResponse(responseCode = "400", description = "Mauvais format de la requête",
                     content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/mission-suggestions")
    public String getMissionSuggestionFromAI(@RequestBody MissionSuggestionDto missionDto) {
        // Construction du prompt pour l'IA
        StringBuilder prompt = new StringBuilder();
        prompt.append("Propose un plan de mission spatiale avec ces critères : ")
              .append("Planète cible : ").append(missionDto.targetPlanet()).append(". ")
              .append("Type de mission : ").append(missionDto.missionType()).append(". ");
        if (missionDto.durationDays() != null) {
            prompt.append("Durée estimée : ").append(missionDto.durationDays()).append(" jours. ");
        }
        if (missionDto.objectives() != null && !missionDto.objectives().isBlank()) {
            prompt.append("Objectifs supplémentaires : ").append(missionDto.objectives()).append(". ");
        }

        return mistralAiService.call(prompt.toString());
    }

    @Operation(
        summary = "Obtenir des conseils d'observation astronomique depuis l'IA",
        description = "Envoie des paramètres (cible, instrument, date, lieu) et reçoit des recommandations"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recommandations d'observation générées par l'IA",
                     content = { @Content(mediaType = "application/json",
                                           schema = @Schema(implementation = String.class)) }),
        @ApiResponse(responseCode = "400", description = "Mauvais format de la requête",
                     content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/observation-tips")
    public String getObservationTipsFromAI(@RequestBody ObservationTipDto tipDto) {
        // Construction du prompt pour l'IA
        StringBuilder prompt = new StringBuilder();
        prompt.append("Donne-moi des conseils pour observer ").append(tipDto.target()).append(". ")
              .append("Instrument utilisé : ").append(tipDto.instrument()).append(". ")
              .append("Date d'observation prévue : ").append(tipDto.observationDate()).append(". ")
              .append("Lieu d'observation : ").append(tipDto.location()).append(". ");

        return mistralAiService.call(prompt.toString());
    }

    @Operation(
        summary = "Dialogue libre avec l'IA (chat)",
        description = "Envoie un prompt libre et reçoit la réponse générée par l'IA"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Réponse de l'IA au prompt fourni",
                     content = { @Content(mediaType = "application/json",
                                           schema = @Schema(implementation = String.class)) }),
        @ApiResponse(responseCode = "400", description = "Prompt invalide",
                     content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        return mistralAiService.call(prompt);
    }
}
