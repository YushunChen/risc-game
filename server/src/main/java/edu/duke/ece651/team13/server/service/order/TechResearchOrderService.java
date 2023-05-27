package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.rulechecker.RuleChecker;
import edu.duke.ece651.team13.server.rulechecker.TechResearchTechLevelChecker;
import edu.duke.ece651.team13.server.rulechecker.TechResearchTechResourceChecker;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Research order
 */
@Service
@RequiredArgsConstructor
public class TechResearchOrderService implements OrderFactory {

    @Autowired
    private final UnitService unitService;

    @Autowired
    private final PlayerService playerService;

    /**
     * Get the default rule checker chain
     * TechResearchTechLevelChecker -> TechResearchTechResourceChecker
     */
    private static RuleChecker getDefaultRuleChecker(){
        RuleChecker resourceChecker = new TechResearchTechResourceChecker(null);
        return new TechResearchTechLevelChecker(resourceChecker);
    }

    /**
     * Validates and executes an order locally within the game.
     */
    @Override
    public void validateAndExecuteLocally(OrderEntity order, GameEntity game) throws IllegalArgumentException {
        RuleChecker ruleChecker = getDefaultRuleChecker();
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        ruleChecker.checkOrder(order, player);
        executeLocally(player, TechResearchTechResourceChecker.getTechCost(player));
    }

    /**
     * Executes the given order on the game locally, updating the necessary game entities.
     */
    private void executeLocally(PlayerEntity player, int techCost){
        player.setTechResource(player.getTechResource() - techCost);
        player.setMaxTechLevel(player.getMaxTechLevel() + 1);
    }

    /**
     * Executes an order on the game entity and save to database
     */
    @Override
    public void executeOnGame(OrderEntity order, GameEntity game) {
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        executeLocally(player, TechResearchTechResourceChecker.getTechCost(player));
        playerService.updatePlayerTechResource(player, player.getTechResource());
        playerService.updatePlayerMaxTechLevel(player, player.getMaxTechLevel());
    }
}
