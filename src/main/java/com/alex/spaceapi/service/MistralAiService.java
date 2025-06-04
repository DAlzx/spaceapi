package com.alex.spaceapi.service;

/**
 * Interface pour appeler Mistral AI avec un prompt.
 */
public interface MistralAiService {
    /**
     * Envoie le prompt à Mistral AI et retourne la réponse textuelle.
     * @param prompt texte décrivant la requête
     * @return réponse textuelle brute de l'IA
     */
    String call(String prompt);
}
