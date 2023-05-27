package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.SpyUnitEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.rulechecker.MoveFoodResourceChecker;
import edu.duke.ece651.team13.server.rulechecker.MoveOwnershipChecker;
import edu.duke.ece651.team13.server.rulechecker.MovePathChecker;
import edu.duke.ece651.team13.server.rulechecker.MoveUnitNumChecker;
import edu.duke.ece651.team13.server.rulechecker.RuleChecker;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.SpyUnitService;
import edu.duke.ece651.team13.server.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.SPY;
import static edu.duke.ece651.team13.server.rulechecker.MoveFoodResourceChecker.getFoodCost;

/**
 * Move order
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MoveOrderService implements OrderFactory {

    @Autowired
    private final UnitService unitService;

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final SpyUnitService spyUnitService;

    /**
     * Get the default rule checker chain
     * MoveOwnershipChecker -> MoveUnitNumChecker -> MoveFoodResourceChecker -> MovePathChecker
     */
    private static RuleChecker getDefaultRuleChecker() {
        RuleChecker foodResourceChecker = new MoveFoodResourceChecker(null);
        RuleChecker pathChecker = new MovePathChecker(foodResourceChecker);
        RuleChecker unitnumChecker = new MoveUnitNumChecker(pathChecker);
        return new MoveOwnershipChecker(unitnumChecker);
    }

    /**
     * Validates and executes an order locally within the game.
     */
    @Override
    public void validateAndExecuteLocally(OrderEntity order, GameEntity game) throws IllegalArgumentException {
        log.info("Locally validating order " + order.getId() + ": " + order.getOrderType().getValue() + " from " +
                order.getSource().getName() + " to " + order.getDestination().getName() + " with " + order.getUnitNum()
                + " units on game " + game.getId());

        RuleChecker ruleChecker = getDefaultRuleChecker();
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        ruleChecker.checkOrder(order, player);
        TerritoryEntity source = game.getMap().getTerritoryEntityById(order.getSource().getId());
        TerritoryEntity destination = game.getMap().getTerritoryEntityById(order.getDestination().getId());
        if(order.getUnitType().equals(SPY)){
            SpyUnitEntity sourceUnit = source.getSpyForPlayer(player);
            SpyUnitEntity destUnit = destination.getSpyForPlayer(player);
            executeLocally(sourceUnit, destUnit, order.getUnitNum(), player, getFoodCost(order));
        }
        else{
            UnitEntity sourceUnit = source.getUnitForType(order.getUnitType());
            UnitEntity destUnit = destination.getUnitForType( order.getUnitType());
            executeLocally(sourceUnit, destUnit, order.getUnitNum(), player, getFoodCost(order));
        }
    }

    /**
     * Executes the given order on the game locally, updating the necessary game entities.
     */
    private void executeLocally(UnitEntity sourceUnit, UnitEntity destUnit, int unitNum, PlayerEntity player, int foodCost) {
        player.setFoodResource(player.getFoodResource() - foodCost);
        sourceUnit.setUnitNum(sourceUnit.getUnitNum() - unitNum);
        destUnit.setUnitNum(destUnit.getUnitNum() + unitNum);
    }

    /**
     * Executes the given order on the game locally, updating the necessary game entities.
     */
    private void executeLocally(SpyUnitEntity sourceUnit, SpyUnitEntity destUnit, int unitNum, PlayerEntity player, int foodCost) {
        player.setFoodResource(player.getFoodResource() - foodCost);
        sourceUnit.setUnitNum(sourceUnit.getUnitNum() - unitNum);
        destUnit.setUnitNum(destUnit.getUnitNum() + unitNum);
    }

    /**
     * Executes an order on the game entity and save to database
     */
    @Override
    public void executeOnGame(OrderEntity order, GameEntity game) {
        log.info("Executing order " + order.getId() + ": " + order.getOrderType().getValue() + " from " +
                order.getSource().getName() + " to " + order.getDestination().getName() + " with " + order.getUnitNum()
                + " units on game " + game.getId());

        TerritoryEntity source = game.getMap().getTerritoryEntityById(order.getSource().getId());
        TerritoryEntity destination = game.getMap().getTerritoryEntityById(order.getDestination().getId());
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());

        if(order.getUnitType().equals(SPY)){
            SpyUnitEntity sourceUnit = source.getSpyForPlayer(player);
            SpyUnitEntity destUnit = destination.getSpyForPlayer(player);
            executeLocally(sourceUnit, destUnit, order.getUnitNum(), player, getFoodCost(order));

            spyUnitService.updateSpyUnit(sourceUnit, sourceUnit.getUnitNum());
            spyUnitService.updateSpyUnit(destUnit, destUnit.getUnitNum());
        }
        else{
            UnitEntity sourceUnit = source.getUnitForType( order.getUnitType());
            UnitEntity destUnit = destination.getUnitForType( order.getUnitType());
            executeLocally(sourceUnit, destUnit, order.getUnitNum(), player, getFoodCost(order));

            unitService.updateUnit(sourceUnit, sourceUnit.getUnitNum());
            unitService.updateUnit(destUnit, destUnit.getUnitNum());
        }

        playerService.updatePlayerFoodResource(player, player.getFoodResource());
    }

}


