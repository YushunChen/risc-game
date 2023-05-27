package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.SpyUnitEntity;
import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.SpyUnitService;
import edu.duke.ece651.team13.server.service.UnitService;
import edu.duke.ece651.team13.server.util.GraphUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.MOVE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MoveOrderServiceTest {

    private MoveOrderService service; //service under test

    @Mock
    private UnitService unitService;

    @Mock
    private PlayerService playerService;

    @Mock
    private SpyUnitService spyUnitService;

    @BeforeEach
    void setUp() {
        service = new MoveOrderService(unitService, playerService, spyUnitService);
    }


    @Test
    void test_validateAndExecuteLocallySuccess() throws IllegalArgumentException {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setFoodResource(140);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        source.setOwner(player1);
        destination.setOwner(player1);
        game.getPlayers().add(player1);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(player1);

        service.validateAndExecuteLocally(order, game);

        assertEquals(5, game.getMap().getTerritories().get(0).getUnits().get(0).getUnitNum());
        assertEquals(15, game.getMap().getTerritories().get(1).getUnits().get(0).getUnitNum());
    }

    @Test
    void test_validateAndExecuteLocallyNoConnectionError() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        TerritoryEntity destination = game.getMap().getTerritories().get(1);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        source.setConnections(connections);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL0, 10));

        OrderEntity order = new OrderEntity();
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setFoodResource(140);
        source.setOwner(player1);
        destination.setOwner(player1);
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(player1);
        game.getPlayers().add(player1);
        //No Connection
        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocallyExtraUnits() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        game.getPlayers().add(player1);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL0, 10));
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        source.setOwner(player1);
        destination.setOwner(player1);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(25);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocallyNotOwnerExcepetion() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity player1 = new PlayerEntity();
        PlayerEntity player2 = new PlayerEntity();
        player1.setId(1L);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(player1);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL0, 10));
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        destination.setOwner(player2);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(player2);
        game.getPlayers().add(player2);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
        source.setOwner(player2);
        destination.setOwner(player1);
        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocally_InsufficientFood() {
        GameEntity game = getGameEntity();
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(player1);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL0, 10));
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        destination.setOwner(player1);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        player1.setFoodResource(0);
        source.setOwner(player1);
        destination.setOwner(player1);
        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(1);
        order.setPlayer(player1);
        game.getPlayers().add(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
        try {
            service.validateAndExecuteLocally(order, game);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid move order: Player doesn't have sufficient food resource.", e.getMessage());
        }
    }

    @Test
    void test_executeOnGame() throws IllegalArgumentException {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setFoodResource(140);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        source.setOwner(player1);
        destination.setOwner(player1);
        game.getPlayers().add(player1);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(player1);


        service.executeOnGame(order, game);
        assertEquals(5, game.getMap().getTerritories().get(0).getUnits().get(0).getUnitNum());
        assertEquals(15, game.getMap().getTerritories().get(1).getUnits().get(0).getUnitNum());

    }

    @Test
    void test_moveSpyUnits_insufficientUnits(){
        GameEntity game = getGameEntity();
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        PlayerEntity player2 = new PlayerEntity();
        player1.setId(2L);
        game.getPlayers().add(player1);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getSpyUnits().add(new SpyUnitEntity(1, player1));
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        source.setOwner(player2);
        destination.setOwner(player2);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.SPY);
        order.setUnitNum(2);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_moveSpyUnits_success(){
        GameEntity game = getGameEntity();
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        PlayerEntity player2 = new PlayerEntity();
        player2.setId(2L);
        game.getPlayers().add(player1);
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getSpyUnits().add(new SpyUnitEntity(1, player1));
        TerritoryEntity destination = game.getMap().getTerritories().get(1);
        source.setOwner(player2);
        destination.setOwner(player2);

        List<TerritoryConnectionEntity> connections = new ArrayList<>();
        connections.add(new TerritoryConnectionEntity(source, destination, 5));
        source.setConnections(connections);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(destination);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.SPY);
        order.setUnitNum(1);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_moveSpyUnits_pathOnlyOneEnemy(){
        GameEntity game = getGameEntity();
        // t1 -(1)- t2 --(2)-- t3 --(1)-- t5
        //   \-(1)- t4 --(1)---/
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        PlayerEntity player2 = new PlayerEntity();
        player2.setId(2L);
        PlayerEntity player3 = new PlayerEntity();
        player3.setId(3L);

        game.getPlayers().add(player1);
        game.getPlayers().add(player2);
        game.getPlayers().add(player3);

        TerritoryEntity t1 = new TerritoryEntity();
        SpyUnitEntity spy = new SpyUnitEntity();
        spy.setOwner(player1);
        spy.setUnitNum(1);
        spy.setTerritory(t1);
        t1.addSpyUnit(spy);
        t1.setOwner(player1);

        TerritoryEntity t2 = new TerritoryEntity();
        t2.setOwner(player1);

        TerritoryEntity t3 = new TerritoryEntity();
        t3.setOwner(player2);

        TerritoryEntity t4 = new TerritoryEntity();
        t4.setOwner(player3);

        TerritoryEntity t5 = new TerritoryEntity();
        t5.setOwner(player2);

        List<TerritoryConnectionEntity> t1Conn = new ArrayList<>();
        t1Conn.add(new TerritoryConnectionEntity(t1, t2, 1));
        t1Conn.add(new TerritoryConnectionEntity(t1, t4, 1));
        t1.setConnections(t1Conn);

        List<TerritoryConnectionEntity> t2Conn = new ArrayList<>();
        t2Conn.add(new TerritoryConnectionEntity(t2, t1, 1));
        t2Conn.add(new TerritoryConnectionEntity(t2, t3, 2));
        t2.setConnections(t2Conn);

        List<TerritoryConnectionEntity> t3Conn = new ArrayList<>();
        t3Conn.add(new TerritoryConnectionEntity(t3, t2, 2));
        t3Conn.add(new TerritoryConnectionEntity(t3, t4, 1));
        t3Conn.add(new TerritoryConnectionEntity(t3, t5, 1));
        t3.setConnections(t3Conn);

        List<TerritoryConnectionEntity> t4Conn = new ArrayList<>();
        t4Conn.add(new TerritoryConnectionEntity(t4, t1, 1));
        t4Conn.add(new TerritoryConnectionEntity(t4, t3, 1));
        t4.setConnections(t4Conn);

        List<TerritoryConnectionEntity> t5Conn = new ArrayList<>();
        t5Conn.add(new TerritoryConnectionEntity(t5, t3, 1));
        t5.setConnections(t5Conn);

        assertEquals(3, GraphUtil.findMinCostForSpy(t1, t3, player1));
        assertEquals(Integer.MAX_VALUE, GraphUtil.findMinCostForSpy(t1, t5, player1));

        OrderEntity order = new OrderEntity();
        order.setSource(t1);
        order.setDestination(t5);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.SPY);
        order.setUnitNum(1);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_moveSpyUnits_pathStartFromEnemy(){
        GameEntity game = getGameEntity();
        // t1 -(1)- t2 --(2)-- t3 --(1)-- t5
        //   \-(1)- t4 --(1)---/
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        PlayerEntity player2 = new PlayerEntity();
        player2.setId(2L);
        PlayerEntity player3 = new PlayerEntity();
        player3.setId(3L);

        game.getPlayers().add(player1);
        game.getPlayers().add(player2);
        game.getPlayers().add(player3);

        TerritoryEntity t1 = new TerritoryEntity();
        t1.setOwner(player2);

        SpyUnitEntity spy = new SpyUnitEntity();
        spy.setOwner(player1);
        spy.setUnitNum(1);
        spy.setTerritory(t1);
        t1.addSpyUnit(spy);

        TerritoryEntity t2 = new TerritoryEntity();
        t2.setOwner(player2);

        TerritoryEntity t3 = new TerritoryEntity();
        t3.setOwner(player1);

        SpyUnitEntity spy3 = new SpyUnitEntity();
        spy3.setOwner(player1);
        spy3.setUnitNum(1);
        spy3.setTerritory(t3);
        t3.addSpyUnit(spy3);

        TerritoryEntity t4 = new TerritoryEntity();
        t4.setOwner(player3);

        TerritoryEntity t5 = new TerritoryEntity();
        t5.setOwner(player2);

        game.getMap().setTerritories(Arrays.asList(t1, t2));

        List<TerritoryConnectionEntity> t1Conn = new ArrayList<>();
        t1Conn.add(new TerritoryConnectionEntity(t1, t2, 1));
        t1Conn.add(new TerritoryConnectionEntity(t1, t4, 1));
        t1.setConnections(t1Conn);

        List<TerritoryConnectionEntity> t2Conn = new ArrayList<>();
        t2Conn.add(new TerritoryConnectionEntity(t2, t1, 1));
        t2Conn.add(new TerritoryConnectionEntity(t2, t3, 2));
        t2.setConnections(t2Conn);

        List<TerritoryConnectionEntity> t3Conn = new ArrayList<>();
        t3Conn.add(new TerritoryConnectionEntity(t3, t2, 2));
        t3Conn.add(new TerritoryConnectionEntity(t3, t4, 1));
        t3Conn.add(new TerritoryConnectionEntity(t3, t5, 1));
        t3.setConnections(t3Conn);

        List<TerritoryConnectionEntity> t4Conn = new ArrayList<>();
        t4Conn.add(new TerritoryConnectionEntity(t4, t1, 1));
        t4Conn.add(new TerritoryConnectionEntity(t4, t3, 1));
        t4.setConnections(t4Conn);

        List<TerritoryConnectionEntity> t5Conn = new ArrayList<>();
        t5Conn.add(new TerritoryConnectionEntity(t5, t3, 1));
        t5.setConnections(t5Conn);

        assertEquals(1, GraphUtil.findMinCostForSpy(t1, t2, player1));

        SpyUnitEntity spyUnit2 = new SpyUnitEntity();
        spyUnit2.setTerritory(t2);
        spyUnit2.setOwner(player1);
        spyUnit2.setUnitNum(0);
        t2.addSpyUnit(spyUnit2);

        SpyUnitEntity spyUnit3 = new SpyUnitEntity();
        spyUnit3.setTerritory(t3);
        spyUnit3.setOwner(player1);
        spyUnit3.setUnitNum(0);
        t3.addSpyUnit(spyUnit3);

        OrderEntity order = new OrderEntity();
        order.setSource(t1);
        order.setDestination(t2);
        order.setOrderType(MOVE);
        order.setUnitType(UnitMappingEnum.SPY);
        order.setUnitNum(1);
        order.setPlayer(player1);

        player1.setFoodResource(100);

        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));

        order.setSource(t3);
        order.setDestination(t2);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }
}