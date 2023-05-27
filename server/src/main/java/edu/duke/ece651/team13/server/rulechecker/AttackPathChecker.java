package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;

import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Checks if an attack order has the correct path, i.e.,
 * the player can only send units to an adjacent territory
 */
public class AttackPathChecker extends RuleChecker {
    public AttackPathChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if (!isAdjacentToTerritory(order.getSource(), order.getDestination())) {
            throw new IllegalArgumentException("Invalid attack order: You can only attack an adjacent territory owned by another player.");
        }
    }

    /**
     * Checks if the destination territory is a neighbor to the source territory
     *
     * @param source      the source territory
     * @param destination the destination territory
     * @return true if it is a valid path and false otherwise
     */
    private boolean isAdjacentToTerritory(TerritoryEntity source, TerritoryEntity destination) {
        Iterator<TerritoryEntity> it = source.getConnections().stream().map(TerritoryConnectionEntity::getDestinationTerritory).collect(Collectors.toList()).iterator();

        while (it.hasNext()) {
            TerritoryEntity neighbor = it.next();
            if (destination == neighbor) {
                return true;
            }
        }
        return false;
    }

}
