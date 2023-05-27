package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.CLOAK_RESEARCH;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CloakResearchServiceTest {

    private CloakResearchService service; //service under test

    @Mock
    private PlayerService playerService;

    @BeforeEach
    void setUp() {service = new CloakResearchService(playerService);}

    @Test
    void test_validateAndExecuteLocallySuccess() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(200);
        player1.setMaxTechLevel(3);

        GameEntity game = getGameEntity();
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setOrderType(CLOAK_RESEARCH);
        order.setPlayer(player1);

        service.validateAndExecuteLocally(order, game);

        assertEquals(true, player1.isCloakResearched());
        assertEquals(0, player1.getTechResource());
    }

    @Test
    void test_validateAndExecuteLocallyIsResearch() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(200);
        player1.setMaxTechLevel(3);
        player1.setCloakResearched(true);

        GameEntity game = getGameEntity();
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setOrderType(CLOAK_RESEARCH);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        player1.setCloakResearched(false);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocallyTechLevelLessThan3() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(200);
        player1.setMaxTechLevel(2);

        GameEntity game = getGameEntity();
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setOrderType(CLOAK_RESEARCH);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        player1.setMaxTechLevel(3);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_validateAndExecuteLocallyTechResourceInsufficient() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setTechResource(100);
        player1.setMaxTechLevel(3);

        GameEntity game = getGameEntity();
        game.getPlayers().add(player1);

        OrderEntity order = new OrderEntity();
        order.setOrderType(CLOAK_RESEARCH);
        order.setPlayer(player1);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));

        player1.setTechResource(200);
        assertDoesNotThrow(() -> service.validateAndExecuteLocally(order, game));
    }

    @Test
    void test_executeOnGame() {
        PlayerEntity player = new PlayerEntity();
        player.setId(1L);
        player.setFoodResource(200);
        player.setMaxTechLevel(3);

        GameEntity game = getGameEntity();
        game.getPlayers().add(player);

        OrderEntity order = new OrderEntity();
        order.setOrderType(CLOAK_RESEARCH);
        order.setPlayer(player);

        service.executeOnGame(order, game);

        assertEquals(true, game.getPlayerEntityById(1L).isCloakResearched());
    }
}