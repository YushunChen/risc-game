package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.SpyUnitEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.rulechecker.CreateSpyOwnershipChecker;
import edu.duke.ece651.team13.server.rulechecker.CreateSpyTechResourceChecker;
import edu.duke.ece651.team13.server.rulechecker.CreateSpyUnitNumChecker;
import edu.duke.ece651.team13.server.rulechecker.CreateSpyUnitTypeChecker;
import edu.duke.ece651.team13.server.rulechecker.RuleChecker;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.SpyUnitService;
import edu.duke.ece651.team13.server.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create Spy Order
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateSpyOrderService implements OrderFactory {
    @Autowired
    private final UnitService unitService;

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final SpyUnitService spyUnitService;

    /**
     * Get the default rule checker chain
     * CreateSpyUnitTypeChecker -> CreateSpyUnitNumChecker -> CreateSpyTechResourceChecker
     */
    private static RuleChecker getDefaultRuleChecker() {
        RuleChecker unitTypeChecker = new CreateSpyUnitTypeChecker(null);
        RuleChecker unitNumChecker = new CreateSpyUnitNumChecker(unitTypeChecker);
        RuleChecker resourceChecker = new CreateSpyTechResourceChecker(unitNumChecker);
        return new CreateSpyOwnershipChecker(resourceChecker);
    }

    @Override
    public void validateAndExecuteLocally(OrderEntity order, GameEntity game) throws IllegalArgumentException {
        log.info("Locally validating order " + order.getId() + ": " + order.getOrderType().getValue() + " in " +
                order.getSource().getName() + " with " + order.getUnitNum()
                + order.getUnitType() + " units in game " + game.getId());

        RuleChecker ruleChecker = getDefaultRuleChecker();
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        ruleChecker.checkOrder(order, player);
        TerritoryEntity source = game.getMap().getTerritoryEntityById(order.getSource().getId());
        UnitEntity sourceUnit = source.getUnitForType(order.getUnitType());
        SpyUnitEntity spyUnit = source.getSpyForPlayer(player);
        executeLocally(sourceUnit, spyUnit, order.getUnitNum(), player, CreateSpyTechResourceChecker.getTechCost(order));
    }

    /**
     * Executes the given order on the game locally, updating the necessary game entities.
     */
    private void executeLocally(UnitEntity sourceUnit, SpyUnitEntity spyUnit, int unitNum, PlayerEntity player, int techCost){
        player.setTechResource(player.getTechResource() - techCost);
        sourceUnit.setUnitNum(sourceUnit.getUnitNum() - unitNum);
        spyUnit.setUnitNum(spyUnit.getUnitNum() + unitNum);
    }

    /**
     * Executes an order on the game entity and save to database
     */
    @Override
    public void executeOnGame(OrderEntity order, GameEntity game) {
        log.info("Executing order " + order.getId() + ": " + order.getOrderType().getValue() + " in " +
                order.getSource().getName() + " with " + order.getUnitNum()
                + order.getUnitType() + " units in game " + game.getId());

        TerritoryEntity source = game.getMap().getTerritoryEntityById(order.getSource().getId());
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        UnitEntity sourceUnit = source.getUnitForType( order.getUnitType());
        SpyUnitEntity spyUnit = source.getSpyForPlayer(player);

        executeLocally(sourceUnit, spyUnit, order.getUnitNum(), player, CreateSpyTechResourceChecker.getTechCost(order));

        unitService.updateUnit(sourceUnit, sourceUnit.getUnitNum());
        spyUnitService.updateSpyUnit(spyUnit, spyUnit.getUnitNum());
        playerService.updatePlayerTechResource(player, player.getTechResource());
    }
}
