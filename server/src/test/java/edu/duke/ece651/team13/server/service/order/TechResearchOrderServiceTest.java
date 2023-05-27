package edu.duke.ece651.team13.server.service.order;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.service.PlayerService;
import edu.duke.ece651.team13.server.service.UnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.TECH_RESEARCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TechResearchOrderServiceTest {

    private TechResearchOrderService service; //service under test

    @Mock
    private UnitService unitService;

    @Mock
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        service = new TechResearchOrderService(unitService, playerService);
    }

    @Test
    void test_validateAndExecuteLocally() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setMaxTechLevel(1);
        player1.setTechResource(20);
        OrderEntity order = new OrderEntity();
        order.setOrderType(TECH_RESEARCH);
        order.setPlayer(player1);

        GameEntity game = getGameEntity();
        game.getPlayers().add(player1);
        service.validateAndExecuteLocally(order, game);

        assertEquals(0, game.getPlayerEntityById(1L).getTechResource());
        assertEquals(2, game.getPlayerEntityById(1L).getMaxTechLevel());

        player1.setTechResource(120);
        service.validateAndExecuteLocally(order, game);
        assertEquals(80, game.getPlayerEntityById(1L).getTechResource());
        assertEquals(3, game.getPlayerEntityById(1L).getMaxTechLevel());

        service.validateAndExecuteLocally(order, game);
        assertEquals(0, game.getPlayerEntityById(1L).getTechResource());
        assertEquals(4, game.getPlayerEntityById(1L).getMaxTechLevel());

        player1.setTechResource(159);
        assertThrows(IllegalArgumentException.class, ()->service.validateAndExecuteLocally(order, game));

        player1.setTechResource(480);
        service.validateAndExecuteLocally(order, game);
        assertEquals(320, game.getPlayerEntityById(1L).getTechResource());
        assertEquals(5, game.getPlayerEntityById(1L).getMaxTechLevel());

        service.validateAndExecuteLocally(order, game);
        assertEquals(0, game.getPlayerEntityById(1L).getTechResource());
        assertEquals(6, game.getPlayerEntityById(1L).getMaxTechLevel());

        player1.setTechResource(480);
        // Exceed max tech level
        assertThrows(IllegalArgumentException.class, () -> service.validateAndExecuteLocally(order, game));
        assertEquals(6, game.getPlayerEntityById(1L).getMaxTechLevel());
    }

    @Test
    void test_initialTechLevel(){
        PlayerEntity player1 = new PlayerEntity("TestPlayer");
        assertEquals(1, player1.getMaxTechLevel());
    }

    @Test
    void test_executeOnGame() {
        PlayerEntity player1 = new PlayerEntity();
        player1.setId(1L);
        player1.setMaxTechLevel(1);
        player1.setTechResource(20);
        OrderEntity order = new OrderEntity();
        order.setOrderType(TECH_RESEARCH);
        order.setPlayer(player1);

        GameEntity game = getGameEntity();
        game.getPlayers().add(player1);
        service.executeOnGame(order, game);

        assertEquals(0, game.getPlayerEntityById(1L).getTechResource());
        assertEquals(2, game.getPlayerEntityById(1L).getMaxTechLevel());
    }

}