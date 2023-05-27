package edu.duke.ece651.team13.server.service;

public interface RoundService {

    /**
     * Plays one round of the game, executing all orders and resolving combats.
     * @param gameId the ID of the game to play a round of
     * @throws IllegalArgumentException if the provided game ID is not valid
     */
    void playOneRound(Long gameId);
}
