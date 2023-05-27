package edu.duke.ece651.team13.server.service.order;


import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.rulechecker.AttackFoodResourceChecker;
import edu.duke.ece651.team13.server.rulechecker.AttackOwnershipChecker;
import edu.duke.ece651.team13.server.rulechecker.AttackPathChecker;
import edu.duke.ece651.team13.server.rulechecker.AttackUnitNumChecker;
import edu.duke.ece651.team13.server.rulechecker.RuleChecker;
import edu.duke.ece651.team13.server.service.AttackerService;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Attack order
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttackOrderService implements OrderFactory {

    @Autowired
    private final UnitService unitService;

    @Autowired
    private final AttackerService attackerService;

    @Autowired
    private final PlayerService playerService;

    /**
     * Get the default rule checker chain
     * AttackOwnershipChecker -> AttackUnitNumChecker -> AttackFoodResourceChecker -> AttackPathChecker
     */
    private static RuleChecker getDefaultRuleChecker() {
        RuleChecker pathChecker = new AttackPathChecker(null);
        RuleChecker foodResourceChecker = new AttackFoodResourceChecker(pathChecker);
        RuleChecker unitNumChecker = new AttackUnitNumChecker(foodResourceChecker);
        return new AttackOwnershipChecker(unitNumChecker);
    }

    /**
     * Validates and executes the given order locally, without sending it to the server.
     * Checks that the given order is valid according to the default rule checker and the current game state,
     * and executes it if it is valid. If the order is invalid, throws an IllegalArgumentException with a message
     * describing the problem.
     * @param order the OrderEntity object representing the order to be executed
     * @param game the GameEntity object representing the current state of the game
     * @throws IllegalArgumentException if the given order is invalid according to the default rule checker
     * or the current game state
     */
    @Override
    public void validateAndExecuteLocally(OrderEntity order, GameEntity game) throws IllegalArgumentException {
        RuleChecker ruleChecker = getDefaultRuleChecker();
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        ruleChecker.checkOrder(order, player);

        TerritoryEntity source = game.getMap().getTerritoryEntityById(order.getSource().getId());
        UnitEntity sourceUnit = source.getUnitForType( order.getUnitType());
        executeLocally(sourceUnit, order.getUnitNum(), player, AttackFoodResourceChecker.getFoodCost(order));
    }

    /**
     * Deducts the specified amount of food resources from the player and reduces the number of units
     * for the given source unit entity by the specified number of units.
     * @param sourceUnit the source unit entity for which the units will be reduced
     * @param unitNum the number of units to be reduced
     * @param player the player entity for which the food resources will be deducted
     * @param foodCost the amount of food resources to be deducted
     */
    private void executeLocally(UnitEntity sourceUnit, int unitNum, PlayerEntity player, int foodCost) {
        player.setFoodResource(player.getFoodResource() - foodCost);
        sourceUnit.setUnitNum(sourceUnit.getUnitNum() - unitNum);
    }

    /**
     * Executes an order on the game entity and save to database
     */
    @Override
    public void executeOnGame(OrderEntity order, GameEntity game) {
        TerritoryEntity source = game.getMap().getTerritoryEntityById(order.getSource().getId());
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        UnitEntity sourceUnit = source.getUnitForType( order.getUnitType());
        executeLocally(sourceUnit, order.getUnitNum(), player, AttackFoodResourceChecker.getFoodCost(order));
        unitService.updateUnit(sourceUnit, sourceUnit.getUnitNum());
        attackerService.addAttacker(order.getDestination(), player, order.getUnitType(), order.getUnitNum());
        playerService.updatePlayerFoodResource(player, player.getFoodResource());
    }

}
