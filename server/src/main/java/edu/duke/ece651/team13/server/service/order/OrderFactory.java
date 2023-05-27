package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;

/**
 * An interface for creating and executing orders
 */
public interface OrderFactory {

    /**
     * Validates and executes an order locally within the game.
     * @param order the order entity to validate and execute
     * @param game the game entity to execute the order within
     * @throws IllegalArgumentException if the order is invalid
     */
    void validateAndExecuteLocally(OrderEntity order, GameEntity game) throws IllegalArgumentException;

    /**
     * Executes an order on the game entity and save to database
     * @param order the order entity to execute
     * @param game the game entity to execute the order within
     */
    void executeOnGame(OrderEntity order, GameEntity game);
}
