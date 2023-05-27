package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.rulechecker.CloakResearchIsResearchedChecker;
import edu.duke.ece651.team13.server.rulechecker.CloakResearchTechLevelChecker;
import edu.duke.ece651.team13.server.rulechecker.CloakResearchTechResourceChecker;
import edu.duke.ece651.team13.server.rulechecker.RuleChecker;
import edu.duke.ece651.team13.server.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cloak research order
 */
@Service
@RequiredArgsConstructor
public class CloakResearchService implements OrderFactory {

    @Autowired
    private final PlayerService playerService;

    /**
     * Get the default rule checker chain
     * CloakResearchIsResearchedChecker -> CloakResearchTechLevelChecker -> CloakResearchTechResourceChecker
     */
    private static RuleChecker getDefaultRuleChecker(){
        RuleChecker resourceChecker = new CloakResearchTechResourceChecker(null);
        RuleChecker techLevelChecker = new CloakResearchTechLevelChecker(resourceChecker);
        return new CloakResearchIsResearchedChecker(techLevelChecker);
    }

    /**
     * Validates and executes an order locally within the game.
     */
    @Override
    public void validateAndExecuteLocally(OrderEntity order, GameEntity game) throws IllegalArgumentException {
        RuleChecker ruleChecker = getDefaultRuleChecker();
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        ruleChecker.checkOrder(order, player);
        executeLocally(player, CloakResearchTechResourceChecker.getTechCost());
    }

    /**
     * Executes the given order on the game locally, updating the necessary game entities.
     */
    private void executeLocally(PlayerEntity player, int techCost){
        player.setTechResource(player.getTechResource() - techCost);
        player.setCloakResearched(true);
    }

    /**
     * Executes an order on the game entity and save to database
     */
    @Override
    public void executeOnGame(OrderEntity order, GameEntity game) {
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        executeLocally(player, CloakResearchTechResourceChecker.getTechCost());
        playerService.updatePlayerTechResource(player, player.getTechResource());
        playerService.updatePlayerCloakResearched(player);
    }
}
