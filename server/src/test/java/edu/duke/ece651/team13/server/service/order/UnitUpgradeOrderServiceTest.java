package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.UnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.UNIT_UPGRADE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UnitUpgradeOrderServiceTest {

    private UnitUpgradeOrderService service; //service under test

    @Mock
    private UnitService unitService;

    @Mock
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        service = new UnitUpgradeOrderService(unitService, playerService);
    }

    @Test
    void test_validateAndExecuteLocallySuccess() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(150);
        owner.setMaxTechLevel(1);


        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().get(0).setUnitNum(5);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL1, 1));
        game.getPlayers().add(owner);


        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(owner);

        service.validateAndExecuteLocally(order, game);

        assertEquals(0, game.getMap().getTerritories().get(0).getUnits().get(0).getUnitNum());
        assertEquals(6, game.getMap().getTerritories().get(0).getUnits().get(1).getUnitNum());
    }

    @Test
    void test_validateAndExecute_verify() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(124);
        owner.setMaxTechLevel(1);


        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().get(0).setUnitNum(18);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL1, 0));
        game.getPlayers().add(owner);


        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(8);
        order.setPlayer(owner);

        service.validateAndExecuteLocally(order, game);

        assertEquals(10, source.getUnitForType(UnitMappingEnum.LEVEL0).getUnitNum());
        assertEquals(8, source.getUnitForType(UnitMappingEnum.LEVEL1).getUnitNum());
        assertEquals(100, owner.getTechResource());
    }


    @Test
    void test_executeOnGameSuccess() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(150);
        owner.setMaxTechLevel(1);


        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().get(0).setUnitNum(5);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL1, 1));
        game.getPlayers().add(owner);


        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(owner);

        service.executeOnGame(order, game);

        assertEquals(0, game.getMap().getTerritories().get(0).getUnits().get(0).getUnitNum());
        assertEquals(6, game.getMap().getTerritories().get(0).getUnits().get(1).getUnitNum());
    }

    @Test
    void test_insufficentunitNo_error() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(150);
        owner.setMaxTechLevel(6);

        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL2, 2));
        game.getPlayers().add(owner);



        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL2);
        order.setUnitNum(5);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, ()->service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_negativeunitNo_error() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(150);
        owner.setMaxTechLevel(6);

        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL2, 2));
        game.getPlayers().add(owner);



        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL2);
        order.setUnitNum(-5);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, ()->service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_destinationTerritory_error() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(150);
        owner.setMaxTechLevel(6);

        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL2, 15));
        game.getPlayers().add(owner);



        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL2);
        order.setUnitNum(5);
        order.setPlayer(owner);
        order.setDestination(new TerritoryEntity());

        assertThrows(IllegalArgumentException.class, ()->service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_territorynotownedby_error() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(150);
        owner.setMaxTechLevel(2);


        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(new PlayerEntity());
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL0, 15));
        game.getPlayers().add(owner);


        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL0);
        order.setUnitNum(5);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, ()->service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_insufficenttechlevel_error() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(150);
        owner.setMaxTechLevel(1);

        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL2, 10));
        game.getPlayers().add(owner);



        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL2);
        order.setUnitNum(5);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, ()->service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_insufficenttechresource_error() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(0);
        owner.setMaxTechLevel(4);

        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL2, 10));
        game.getPlayers().add(owner);



        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL2);
        order.setUnitNum(5);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, ()->service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_maxLevel_error() throws IllegalArgumentException {
        GameEntity game = getGameEntity();
        PlayerEntity owner = new PlayerEntity();
        owner.setId(1L);
        owner.setTechResource(150);
        owner.setMaxTechLevel(6);

        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(owner);
        source.getUnits().add(new UnitEntity(UnitMappingEnum.LEVEL6, 5));
        game.getPlayers().add(owner);


        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(UNIT_UPGRADE);
        order.setUnitType(UnitMappingEnum.LEVEL6);
        order.setUnitNum(1);
        order.setPlayer(owner);

        assertThrows(IllegalArgumentException.class, ()->service.validateAndExecuteLocally(order, game));

    }


}