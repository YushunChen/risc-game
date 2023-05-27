package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.dto.OrdersDTO;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;

import java.util.List;

public interface OrderService {

    /**
     * Retrieves a list of orders associated with the specified player.
     * @param playerEntity the player entity for which to retrieve orders
     * @return a list of orders associated with the specified player
     */
    List<OrderEntity> getOrdersByPlayer(PlayerEntity playerEntity);

    /**
     * Deletes all orders associated with the given player entity.
     * @param playerEntity The player entity whose orders are to be deleted.
     */
    void deleteOrdersByPlayer(PlayerEntity playerEntity);

    /**
     * Validates the orders and adds them to the system.
     * @param orders the OrdersDTO object containing the orders to be added
     * @param playerId the ID of the player submitting the orders
     * @throws IllegalArgumentException if the orders are invalid or if the player is not valid
     */
    void validateAndAddOrders(OrdersDTO orders, Long playerId) throws IllegalArgumentException;

}
