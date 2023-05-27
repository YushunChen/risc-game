package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.TerritoryViewEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.GameStatusEnum;
import edu.duke.ece651.team13.server.enums.OrderMappingEnum;
import edu.duke.ece651.team13.server.enums.PlayerStatusEnum;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.service.order.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.CARD_CONQUERING_WARRIORS;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.CARD_FAMINE;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.CARD_UNBREAKABLE_DEFENCE;
import static edu.duke.ece651.team13.server.enums.PlayerStatusEnum.PLAYING;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoundServiceImpl implements RoundService {

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final MoveOrderService moveOrder;

    @Autowired
    private final AttackOrderService attackOrder;

    @Autowired
    private final UnitUpgradeOrderService unitUpgradeOrder;

    @Autowired
    private final TechResearchOrderService techResearchOrder;

    @Autowired
    private final CloakResearchService cloakResearchOrder;

    @Autowired
    private final CloakService cloakOrder;

    @Autowired
    private final CardUnbreakableDefenseService cardUnbreakableDefenseService;

    @Autowired
    private final CombatResolutionService combatResolutionService;

    @Autowired
    private final TerritoryService territoryService;

    @Autowired
    private final UnitService unitService;

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final GameService gameService;

    @Autowired
    private final TerritoryViewService territoryViewService;

    @Autowired
    private final CreateSpyOrderService createSpyOrder;

    private void executePlayersOrders(GameEntity game) {
        for (PlayerEntity player : game.getPlayers()) {
            List<OrderEntity> orders = orderService.getOrdersByPlayer(player);

            orders.stream()
                    .filter(order -> order.getOrderType().equals(OrderMappingEnum.UNIT_UPGRADE))
                    .forEach(order -> unitUpgradeOrder.executeOnGame(order, game));

            orders.stream()
                    .filter(order -> order.getOrderType().equals(OrderMappingEnum.MOVE))
                    .forEach(order -> moveOrder.executeOnGame(order, game));

            orders.stream()
                    .filter(order -> order.getOrderType().equals(OrderMappingEnum.ATTACK))
                    .forEach(order -> attackOrder.executeOnGame(order, game));

            orders.stream()
                    .filter(order -> order.getOrderType().equals(OrderMappingEnum.TECH_RESEARCH))
                    .forEach(order -> techResearchOrder.executeOnGame(order, game));

            orders.stream()
                    .filter(order -> order.getOrderType().equals(OrderMappingEnum.CREATE_SPY))
                    .forEach(order -> createSpyOrder.executeOnGame(order, game));

            orders.stream()
                    .filter(order -> order.getOrderType().equals(OrderMappingEnum.CLOAK_RESEARCH))
                    .forEach(order -> cloakResearchOrder.executeOnGame(order, game));

            orders.stream()
                    .filter(order -> order.getOrderType().equals(OrderMappingEnum.CLOAK))
                    .forEach(order -> cloakOrder.executeOnGame(order, game));
        }
    }

    private void executeUnbreakableDefense(GameEntity game){
        for (PlayerEntity player : game.getPlayers()) {
            List<OrderEntity> orders = orderService.getOrdersByPlayer(player);

            orders.stream()
                    .filter(order -> order.getOrderType().equals(CARD_UNBREAKABLE_DEFENCE))
                    .forEach(order -> cardUnbreakableDefenseService.executeOnGame(order, game));
        }
    }

    private void resolveCombatForGame(GameEntity game) {
        executeUnbreakableDefense(game);
        game.getMap().getTerritories().forEach(combatResolutionService::resolveCombot);
    }

    private void updatePlayerStatus(GameEntity game) {
        for (PlayerEntity player : game.getPlayers()) {
            if (territoryService.getTerritoriesByPlayer(player).isEmpty()) {
                playerService.updatePlayerStatus(player, PlayerStatusEnum.LOSE);
            }
        }
    }

    private void updateGameStatus(GameEntity game) {
        long noOfPlayersPlaying = game.getPlayers().stream().filter(player -> player.getStatus().equals(PLAYING)).count();
        if (noOfPlayersPlaying <= 1) {
            gameService.updateGameRoundAndStatus(game, GameStatusEnum.ENDED, game.getRoundNo() + 1);
        } else {
            gameService.updateGameRoundAndStatus(game, GameStatusEnum.PLAYING, game.getRoundNo() + 1);
        }
    }

    private void updateResourceForPlayers(List<PlayerEntity> players) {
        for (PlayerEntity player : players) {
            List<OrderEntity> orders = orderService.getOrdersByPlayer(player);
            //Adding units only if there is no Famine
            if (orders.stream().noneMatch(order -> order.getOrderType().equals(CARD_FAMINE))) {
                List<TerritoryEntity> territoryEntities = territoryService.getTerritoriesByPlayer(player);
                int foodProduction = 0;
                int techProduction = 0;
                for (TerritoryEntity territory : territoryEntities) {
                    foodProduction += territory.getFoodProduction();
                    techProduction += territory.getTechProduction();
                }
                playerService.updatePlayerFoodResource(player, player.getFoodResource() + foodProduction);
                playerService.updatePlayerTechResource(player, player.getTechResource() + techProduction);
            }
        }
    }

    /**
     * Increment the basic unit number for the players
     * @param players is the set of players
     */
    private void addUnitForPlayers(List<PlayerEntity> players){
        for (PlayerEntity player : players) {
            //Checking for Level 6  bonus upgrade
            List<OrderEntity> orders = orderService.getOrdersByPlayer(player);
            boolean conqueringWarriorsUpdate = orders.stream().anyMatch(order -> order.getOrderType().equals(CARD_CONQUERING_WARRIORS));

            List<TerritoryEntity> territoryEntities = territoryService.getTerritoriesByPlayer(player);
            for (TerritoryEntity territory : territoryEntities){
                UnitEntity basicUnitEntity = territory.getUnitForType( UnitMappingEnum.LEVEL0);
                unitService.updateUnit(basicUnitEntity, basicUnitEntity.getUnitNum() + 1);

                //adding Level 6 for the conquering Warriors update
                if(conqueringWarriorsUpdate){
                    UnitEntity conqueringWarriorsUnitEntity = territory.getUnitForType( UnitMappingEnum.LEVEL6);
                    unitService.updateUnit(conqueringWarriorsUnitEntity, conqueringWarriorsUnitEntity.getUnitNum() + 1);
                }
            }
        }
    }

    private void clearOrders(GameEntity game){
        for(PlayerEntity player:game.getPlayers()){
            orderService.deleteOrdersByPlayer(player);
        }
    }

    /**
     * update territoryViews for each territory after each round
     * @param territoryEntities list of all territories in the map
     */
    private void updateTerritoryViewForTerritories(List<TerritoryEntity> territoryEntities){
        for(TerritoryEntity territory: territoryEntities){
            for(TerritoryViewEntity territoryView: territory.getTerritoryViews()){
                territoryViewService.updateTerritoryView(territoryView);
            }
        }
    }

    /**
     * decrease "remainCloak" by 1 if the territory is cloaked
     * @param territoryEntities list of all territories in the map
     */
    private void updateTerritoryRemainingCloak(List<TerritoryEntity> territoryEntities){
        for(TerritoryEntity territory: territoryEntities){
            if(territory.getRemainingCloak()>0){
                territoryService.updateTerritoryRemainingCloak(territory, territory.getRemainingCloak()-1);
            }
        }
    }

    @Override
    @Async
    @EventListener
    public void playOneRound(Long gameId) {
        log.info("Executing Round for Game Id :" + gameId);
        GameEntity game = gameService.getGame(gameId);
        executePlayersOrders(game);

        if (game.getRoundNo() >= 1) {
            resolveCombatForGame(game);
            updateResourceForPlayers(game.getPlayers());
            addUnitForPlayers(game.getPlayers());
            updatePlayerStatus(game);
            updateTerritoryViewForTerritories(game.getMap().getTerritories());
            updateTerritoryRemainingCloak(game.getMap().getTerritories());
        }

        clearOrders(game);
        updateGameStatus(game);
    }
}