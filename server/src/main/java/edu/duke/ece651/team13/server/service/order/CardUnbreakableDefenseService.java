package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.rulechecker.CardUnbreakableDefenseOwnershipChecker;
import edu.duke.ece651.team13.server.rulechecker.RuleChecker;
import edu.duke.ece651.team13.server.service.AttackerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Research order
 */
@Service
@RequiredArgsConstructor
public class CardUnbreakableDefenseService implements OrderFactory {



    @Autowired
    private final AttackerService attackerService;


    /**
     * Get the default rule checker chain
     * CardUnbreakableDefenseOwnershipChecker
     */
    private static RuleChecker getDefaultRuleChecker() {
        return new CardUnbreakableDefenseOwnershipChecker(null);
    }

    /**
     * Validates and executes an order locally within the game.
     */
    @Override
    public void validateAndExecuteLocally(OrderEntity order, GameEntity game) throws IllegalArgumentException {
        RuleChecker ruleChecker = getDefaultRuleChecker();
        PlayerEntity player = game.getPlayerEntityById(order.getPlayer().getId());
        ruleChecker.checkOrder(order, player);
    }


    /**
     * Executes an order on the game entity and save to database
     */
    @Override
    public void executeOnGame(OrderEntity order, GameEntity game) {
        attackerService.clearAttackers(order.getSource());
    }
}
