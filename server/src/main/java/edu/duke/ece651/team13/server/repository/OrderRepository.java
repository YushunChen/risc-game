package edu.duke.ece651.team13.server.repository;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for the OrderEntity
 */
@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {

    /**
     * Retrieves a list of orders based on the player who issued them.
     * @param player the player who issued the orders
     * @return a list of orders issued by the player
     */
    List<OrderEntity> findByPlayer(PlayerEntity player);

    /**
     * Deletes all orders issued by a specific player.
     * @param player the player whose orders will be deleted
     */
    void deleteByPlayer(PlayerEntity player);
}
