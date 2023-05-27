package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.rulechecker.*;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.TerritoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cloak order
 */
@Service
@RequiredArgsConstructor
public class CloakService implements OrderFactory {

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final TerritoryService territoryService;

    /**
     * Get the default rule checker chain
     * CloakIsResearchedChecker -> CloakIsCloakedChecker -> CloakOwnershipChecker -> CloakTechResourceChecker
     */
    private static RuleChecker getDefaultRuleChecker(){
        RuleChecker resourceChecker = new CloakTechResourceChecker(null);
        RuleChecker ownershipChecker = new CloakOwnershipChecker(resourceChecker);
        RuleChecker isCloakedChecker = new CloakIsCloakedChecker(ownershipChecker);
        return new CloakIsResearchedChecker(isCloakedChecker);
    }

    /**
     * Validates and executes an order locally within the game.
     */
    @Override
    public void validateAndExecuteLocally(OrderEntity order, GameEntity game) throws IllegalArgumentException {
        RuleChecker ruleChecker = getDefaultRuleChecker();
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        ruleChecker.checkOrder(order, player);
        executeLocally(order.getSource(), player, CloakTechResourceChecker.getTechCost());
    }

    /**
     * Executes the given order on the game locally, updating the necessary game entities.
     */
    private void executeLocally(TerritoryEntity territory, PlayerEntity player, int techCost){
        player.setTechResource(player.getTechResource() - techCost);
        territory.setRemainingCloak(3);
    }

    /**
     * Executes an order on the game entity and save to database
     */
    @Override
    public void executeOnGame(OrderEntity order, GameEntity game) {
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        executeLocally(order.getSource(), player, CloakResearchTechResourceChecker.getTechCost());
        playerService.updatePlayerTechResource(player, player.getTechResource());
        //hide target territory for 3 turns from view
        territoryService.updateTerritoryRemainingCloak(order.getSource(), 3);
    }
}
