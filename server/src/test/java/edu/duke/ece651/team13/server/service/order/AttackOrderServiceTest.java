package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.service.AttackerService;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.UnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.ATTACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AttackOrderServiceTest {

    private AttackOrderService service; //service under test

    @Mock
    private UnitService unitService;

    @Mock
    private AttackerService attackerService;

    @Mock
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        service = new AttackOrderService(unitService, attackerService, playerService);
    }

    @Test
    void test_validateAndExecuteLocallySuccess() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setFoodResource(150);
        PlayerEntity opponent = new PlayerEntity();
        opponent.setId(2L);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        destination.setOwner(opponent);
        game.getPlayers().add(owner);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(ATTACK);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(owner);

        service.validateAndExecuteLocally(order, game);

        assertEquals(5, game.getMap().getTerritories().get(0).getUnits().get(0).getUnitNum());
    }


    @Test
    void test_validateAndExecuteLocallyNottAdjacnet() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setFoodResource(150);
        PlayerEntity opponent = new PlayerEntity();
        opponent.setId(2L);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        destination.setOwner(opponent);
        game.getPlayers().add(owner);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();

        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(ATTACK);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

    }

    @Test
    void test_validateAndExecuteLocallyNotEnoughUnits() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        PlayerEntity opponent = new PlayerEntity();
        opponent.setId(2L);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        destination.setOwner(opponent);
        game.getPlayers().add(owner);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(ATTACK);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(25);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
    }


    @Test
    void test_validateAndExecuteLocallyNotOnwedAttackingSame() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        PlayerEntity opponent = new PlayerEntity();
        opponent.setId(2L);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(opponent);
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        destination.setOwner(opponent);
        game.getPlayers().add(owner);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(ATTACK);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
        source.setOwner(owner);
        destination.setOwner(owner);
        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

    }

    @Test
    void test_executeOnGame() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setFoodResource(150);
        PlayerEntity opponent = new PlayerEntity();
        opponent.setId(2L);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        destination.setOwner(opponent);
        game.getPlayers().add(owner);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(ATTACK);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(owner);

        service.executeOnGame(order, game);

        assertEquals(5, game.getMap().getTerritories().get(0).getUnits().get(0).getUnitNum());
    }


}