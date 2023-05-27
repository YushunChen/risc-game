package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.TerritoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.CLOAK;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CloakServiceTest {

    private CloakService service;

    @Mock
    private PlayerService playerService;

    @Mock
    private TerritoryService territoryService;

    @BeforeEach
    void setUp() {service = new CloakService(playerService, territoryService);}

    @Test
    void validateAndExecuteLocallySuccess() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setCloakResearched(true);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(player1);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setOrderType(CLOAK);
        order.setPlayer(player1);
        order.setSource(source);

        service.validateAndExecuteLocally(order, game);

        assertEquals(3, source.getRemainingCloak());
        assertEquals(0, player1.getTechResource());
    }

    @Test
    void test_validateAndExecuteLocallyIsCloaked() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setCloakResearched(true);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(player1);
        source.setRemainingCloak(3);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setOrderType(CLOAK);
        order.setPlayer(player1);
        order.setSource(source);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        source.setRemainingCloak(0);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocallyIsNotResearch() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setCloakResearched(false);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(player1);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setOrderType(CLOAK);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        player1.setCloakResearched(true);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocallyTechResourceInsufficient() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(50);
        player1.setMaxTechLevel(3);
        player1.setCloakResearched(true);

        GameEntity game = getGameEntity();
        TerritoryEntity source = game.getMap().getTerritories().get(0);
        source.setOwner(player1);
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setSource(source);
        order.setOrderType(CLOAK);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        player1.setTechResource(100);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }
}