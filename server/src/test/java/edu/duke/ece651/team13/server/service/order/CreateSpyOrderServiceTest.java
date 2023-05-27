package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.SpyUnitEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.SpyUnitService;
import edu.duke.ece651.team13.server.service.UnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.CREATE_SPY;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL0;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL1;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CreateSpyOrderServiceTest {
    private CreateSpyOrderService service; //service under test

    @Mock
    private UnitService unitService;

    @Mock
    private PlayerService playerService;

    @Mock
    private SpyUnitService spyUnitService;

    @BeforeEach
    void setUp() {
        service = new CreateSpyOrderService(unitService, playerService, spyUnitService);
    }

    @Test
    void test_validateAndExecuteLocallySuccess() throws IllegalArgumentException{
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setMaxTechLevel(1);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getUnits().add(getUnit(1, LEVEL1));
        source.getSpyUnits().add(getSpyUnit(0, player1));

        source.setOwner(player1);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(CREATE_SPY);
        order.setUnitType(LEVEL1);
        order.setUnitNum(1);
        order.setPlayer(player1);

        service.validateAndExecuteLocally(order, game);

        assertEquals(0, source.getUnitForType(LEVEL1).getUnitNum());
        assertEquals(1, source.getSpyForPlayer(player1).getUnitNum());
        assertEquals(0, player1.getTechResource());
    }

    @Test
    void test_validateAndExecute_IncorrectUnitType() throws IllegalArgumentException{
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setMaxTechLevel(1);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getUnits().add(getUnit(1, LEVEL1));
        source.getSpyUnits().add(getSpyUnit(0, player1));

        source.setOwner(player1);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(CREATE_SPY);
        order.setUnitType(LEVEL0);
        order.setUnitNum(1);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        order.setUnitType(LEVEL1);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecute_InsufficientTechResource() throws IllegalArgumentException{
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(99);
        player1.setMaxTechLevel(1);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getUnits().add(getUnit(1, LEVEL1));
        source.getSpyUnits().add(getSpyUnit(0, player1));

        source.setOwner(player1);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(CREATE_SPY);
        order.setUnitType(LEVEL1);
        order.setUnitNum(1);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        player1.setTechResource(100);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocally_InsufficientUnits() throws IllegalArgumentException{
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setMaxTechLevel(1);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getUnits().add(getUnit(1, LEVEL1));
        source.getSpyUnits().add(getSpyUnit(0, player1));

        source.setOwner(player1);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(CREATE_SPY);
        order.setUnitType(LEVEL1);
        order.setUnitNum(2);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        order.setUnitNum(1);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocally_OwnershipError() throws IllegalArgumentException{
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setMaxTechLevel(1);

        PlayerEntity player2 = new PlayerEntity();

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getUnits().add(getUnit(1, LEVEL1));
        source.getSpyUnits().add(getSpyUnit(0, player1));

        source.setOwner(player2);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setDestination(null);
        order.setOrderType(CREATE_SPY);
        order.setUnitType(LEVEL1);
        order.setUnitNum(1);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        source.setOwner(player1);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocally_DestNotNullError() throws IllegalArgumentException{
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setMaxTechLevel(1);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.getUnits().add(getUnit(1, LEVEL1));
        source.getSpyUnits().add(getSpyUnit(0, player1));

        source.setOwner(player1);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        TerritoryEntity dest = new TerritoryEntity();
        order.setDestination(dest);
        order.setOrderType(CREATE_SPY);
        order.setUnitType(LEVEL1);
        order.setUnitNum(1);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        order.setDestination(null);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    private static UnitEntity getUnit(int unitNum, UnitMappingEnum unitType) {
        UnitEntity unit = new UnitEntity();
        unit.setUnitNum(unitNum);
        unit.setUnitType(unitType);
        return unit;
    }

    private static SpyUnitEntity getSpyUnit(int unitNum, PlayerEntity owner){
        SpyUnitEntity spyUnit = new SpyUnitEntity();
        spyUnit.setUnitNum(unitNum);
        spyUnit.setOwner(owner);
        return spyUnit;
    }
}