package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.dto.OrderDTO;
import edu.duke.ece651.team13.server.dto.OrdersDTO;
import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.enums.OrderMappingEnum;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.repository.OrderRepository;
import edu.duke.ece651.team13.server.service.order.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static edu.duke.ece651.team13.server.enums.GameStatusEnum.ENDED;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.*;
import static edu.duke.ece651.team13.server.enums.PlayerStatusEnum.LOSE;
import static edu.duke.ece651.team13.server.enums.PlayerStatusEnum.PLAYING;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    private final OrderRepository repository;

    @Autowired
    private final GameService gameService;

    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final MoveOrderService moveOrder;

    @Autowired
    private final AttackOrderService attackOrder;

    @Autowired
    private final UnitUpgradeOrderService unitUpgradeOrder;

    @Autowired
    private final TechResearchOrderService techResearchOrder;

    @Autowired
    private final CardUnbreakableDefenseService cardUnbreakableDefenseOrder;

    @Autowired
    private final CreateSpyOrderService createSpyOrder;

    @Autowired
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    private final CloakResearchService researchCloakOrder;

    @Autowired
    private final CloakService cloakOrder;


    @Override
    public List<OrderEntity> getOrdersByPlayer(PlayerEntity playerEntity) {
        return repository.findByPlayer(playerEntity);
    }

    @Override
    @Transactional
    public void deleteOrdersByPlayer(PlayerEntity playerEntity) {
        repository.deleteByPlayer(playerEntity);
    }

    private Boolean isGameReadyForRoundExecution(Long Id) {
        GameEntity game = gameService.getGame(Id); //Fetching a new game after detaching from the service.
        //Checking if all the players who are active have submitted orders
        for (PlayerEntity player : game.getPlayers()) {
            if (player.getStatus().equals(PLAYING) && getOrdersByPlayer(player).size() == 0) {
                return false;
            }
        }
        return true;
    }

    private List<OrderEntity> getOrderEntityList(OrdersDTO orders, GameEntity game, PlayerEntity player) {
        List<OrderEntity> orderEntities = new ArrayList<>();
        for (OrderDTO orderDTO : orders.getOrders()) {
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setPlayer(player);
            orderEntity.setOrderType(OrderMappingEnum.findByValue(orderDTO.getOrderType()));

            if (!(orderDTO.getOrderType().equals(CLOAK_RESEARCH.getValue()) || orderDTO.getOrderType().equals(DONE.getValue()) || orderDTO.getOrderType().equals(TECH_RESEARCH.getValue()) || orderDTO.getOrderType().equals(CARD_CONQUERING_WARRIORS.getValue()) || orderDTO.getOrderType().equals(CARD_FAMINE.getValue()) || orderDTO.getOrderType().equals(CARD_NO_LUCK.getValue()))) {
                TerritoryEntity source = game.getMap().getTerritoryEntityById(orderDTO.getSourceTerritoryId());
                orderEntity.setSource(source);
            }

            if (!(orderDTO.getOrderType().equals(CLOAK_RESEARCH.getValue()) || orderDTO.getOrderType().equals(CLOAK.getValue()) || orderDTO.getOrderType().equals(CREATE_SPY.getValue())
                    || orderDTO.getOrderType().equals(DONE.getValue()) || orderDTO.getOrderType().equals(TECH_RESEARCH.getValue())
                    || orderDTO.getOrderType().equals(UNIT_UPGRADE.getValue()) || orderDTO.getOrderType().equals(CARD_CONQUERING_WARRIORS.getValue())
                    || orderDTO.getOrderType().equals(CARD_FAMINE.getValue()) || orderDTO.getOrderType().equals(CARD_UNBREAKABLE_DEFENCE.getValue())
                    || orderDTO.getOrderType().equals(CARD_NO_LUCK.getValue()))) {
                TerritoryEntity destination = game.getMap().getTerritoryEntityById(orderDTO.getDestinationTerritoryId());
                orderEntity.setDestination(destination);
            }

            if (!(orderDTO.getOrderType().equals(CLOAK_RESEARCH.getValue()) || orderDTO.getOrderType().equals(CLOAK.getValue()) || orderDTO.getOrderType().equals(DONE.getValue()) || orderDTO.getOrderType().equals(TECH_RESEARCH.getValue()) || orderDTO.getOrderType().equals(CARD_CONQUERING_WARRIORS.getValue()) || orderDTO.getOrderType().equals(CARD_FAMINE.getValue()) || orderDTO.getOrderType().equals(CARD_UNBREAKABLE_DEFENCE.getValue()) || orderDTO.getOrderType().equals(CARD_NO_LUCK.getValue()))) {
                orderEntity.setUnitNum(orderDTO.getUnitNum());
                orderEntity.setUnitType(UnitMappingEnum.findByValue(orderDTO.getUnitType()));
            }
            orderEntities.add(orderEntity);
        }
        return orderEntities;
    }

    @Transactional
    private void saveOrders(List<OrderEntity> orderEntityList) {
        //Save order list
        for (OrderEntity order : orderEntityList) {
            repository.save(order);
        }
    }

    @Override
    public void validateAndAddOrders(OrdersDTO orders, Long playerId) throws IllegalArgumentException {
        PlayerEntity player = playerService.getPlayer(playerId);
        log.info("Just after detach Entity in order get" + entityManager.contains(player));

        if (player.getStatus().equals(LOSE)) {
            throw new IllegalArgumentException("Player has already lost he cannot issue a order.");
        }

        if (getOrdersByPlayer(player).size() != 0) {
            throw new IllegalArgumentException("Player has already submitted orders for this round.");
        }

        GameEntity game = player.getGame();

        if (game.getStatus().equals(ENDED)) {
            throw new IllegalArgumentException("Game has already ended no new orders could be issued.");
        }

        if (game.getRoundNo() == 0 && orders
                .getOrders().stream()
                .anyMatch(orderDTO -> !(orderDTO.getOrderType().equals(MOVE.getValue()) || orderDTO.getOrderType().equals(DONE.getValue())))) {
            throw new IllegalArgumentException("Only Move or Done Orders can be issued during initialising round.");
        }

        List<OrderEntity> orderEntityList = getOrderEntityList(orders, game, player);

        //Validate Unit upgrade order
        for (OrderEntity order : orderEntityList) {
            if (order.getOrderType().equals(UNIT_UPGRADE)) {
                unitUpgradeOrder.validateAndExecuteLocally(order, game);
            }
        }

        //Validate Tech research order
        boolean hasResearch = false;
        for (OrderEntity order : orderEntityList) {
            if (order.getOrderType().equals(TECH_RESEARCH)) {
                if (hasResearch) {
                    throw new IllegalArgumentException("Invalid technology research order: The player " +
                            "issues more than one technology resource orders this round.");
                }
                hasResearch = true;
                techResearchOrder.validateAndExecuteLocally(order, game);
            }
        }

        //Validate Move Order
        for (OrderEntity order : orderEntityList) {
            if (order.getOrderType().equals(MOVE)) {
                moveOrder.validateAndExecuteLocally(order, game);
            }
        }

        //Validate Attack order
        for (OrderEntity order : orderEntityList) {
            if (order.getOrderType().equals(ATTACK)) {
                attackOrder.validateAndExecuteLocally(order, game);
            }
        }

        //Validate Card Unbreakable Defense order
        for (OrderEntity order : orderEntityList) {
            if (order.getOrderType().equals(CARD_UNBREAKABLE_DEFENCE)) {
                cardUnbreakableDefenseOrder.validateAndExecuteLocally(order, game);
            }
        }

        //Validate Create Spy order
        for (OrderEntity order : orderEntityList) {
            if (order.getOrderType().equals(CREATE_SPY)) {
                createSpyOrder.validateAndExecuteLocally(order, game);
            }
        }
                
        //Validate Cloak Research order
        for(OrderEntity order: orderEntityList){
            if(order.getOrderType().equals(CLOAK_RESEARCH)){
                researchCloakOrder.validateAndExecuteLocally(order, game);
            }
        }

        //Validate Cloak order
        for(OrderEntity order: orderEntityList){
            if(order.getOrderType().equals(CLOAK)){
                cloakOrder.validateAndExecuteLocally(order, game);
            }
        }

        Long gameId = game.getId(); //etching gameId before detaching
        entityManager.detach(game); //Added to detach game entity from Persistent manager so that changes in game is not updated to db
        saveOrders(orderEntityList);

        if (isGameReadyForRoundExecution(gameId)) {
            eventPublisher.publishEvent(gameId);
        }

    }

}
