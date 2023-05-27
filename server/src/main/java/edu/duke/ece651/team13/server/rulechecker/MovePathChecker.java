package edu.duke.ece651.team13.server.rulechecker;

import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.util.GraphUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

import static edu.duke.ece651.team13.server.util.GraphUtil.findMinCostForSpy;

/**
 * Checks if there is a valid path between the source and
 * destination territories in a move order.
 */
@Slf4j
public class MovePathChecker extends RuleChecker {
    public MovePathChecker(RuleChecker next) {
        super(next);
    }

    @Override
    protected void checkMyRule(OrderEntity order, PlayerEntity player) throws IllegalArgumentException {
        if (order.getUnitType().equals(UnitMappingEnum.SPY)) {
            if(!order.getSource().getOwner().equals(player)){
                if(!isAdjacentToTerritory(order.getSource(), order.getDestination())){
                    throw new IllegalArgumentException("Invalid move order: There is not a valid path between the src and dst.");
                }
            }
            else{
                int minDistance = findMinCostForSpy(order.getSource(), order.getDestination(), order.getPlayer());
                log.info("Min distance for spy is: " + minDistance);
                // Spy units have special move rules: in enemy territories, can only move to one adjacent territory at a time
                if (minDistance == Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("Invalid move order: There is not a valid path between the src and dst.");
                }
            }
        } else if (!GraphUtil.hasPath(order.getSource(), order.getDestination())) {
            throw new IllegalArgumentException("Invalid move order: There is not a valid path between the src and dst.");
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
        for (TerritoryEntity neighbor : source.getConnections().stream().map(TerritoryConnectionEntity::getDestinationTerritory).collect(Collectors.toList())) {
            if (destination == neighbor) {
                return true;
            }
        }
        return false;
    }
}
