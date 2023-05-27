package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.dto.OrderDTO;
import edu.duke.ece651.team13.server.dto.OrdersDTO;
import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.MapEntity;
import edu.duke.ece651.team13.server.entity.OrderEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.enums.GameStatusEnum;
import edu.duke.ece651.team13.server.enums.PlayerStatusEnum;
import edu.duke.ece651.team13.server.repository.OrderRepository;
import edu.duke.ece651.team13.server.service.order.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.ATTACK;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.MOVE;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.TECH_RESEARCH;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.UNIT_UPGRADE;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL0;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private OrderService service; //service under test
    @Mock
    private EntityManager entityManager;

    @Mock
    private OrderRepository repository;

    @Mock
    private GameService gameService;

    @Mock
    private PlayerService playerService;

    @Mock
    private MoveOrderService moveOrder;

    @Mock
    private AttackOrderService attackOrder;

    @Mock
    private UnitUpgradeOrderService unitUpgradeOrder;

    @Mock
    private TechResearchOrderService techResearchOrder;

    @Mock
    private CardUnbreakableDefenseService cardUnbreakableDefenseOrder;

    @Mock
    private CreateSpyOrderService createSpyOrder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private CloakResearchService cloakResearchOrder;

    @Mock
    private CloakService cloakOrder;


    @BeforeEach
    void setUp() {
        service = new OrderServiceImpl(entityManager, repository, gameService, playerService, moveOrder, attackOrder, unitUpgradeOrder, techResearchOrder, cardUnbreakableDefenseOrder, createSpyOrder, eventPublisher, cloakResearchOrder, cloakOrder);
    }

    @Test
    void getOrdersByPlayerTest() {
        List<OrderEntity> orders = new ArrayList<>();
        orders.add(new OrderEntity());
        when(repository.findByPlayer(any())).thenReturn(orders);
        List<OrderEntity> actual = service.getOrdersByPlayer(new PlayerEntity("Red"));
        assertEquals(orders, actual);
    }

    @Test
    void deleteOrdersByPlayerTest() {

        service.deleteOrdersByPlayer(new PlayerEntity("Red"));
        verify(repository, times(1)).deleteByPlayer(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void validateAndAddOrders_LoosingPlayerIssueOrderTest() {
        PlayerEntity losePlayer = new PlayerEntity();
        losePlayer.setStatus(PlayerStatusEnum.LOSE);
        losePlayer.setId(1L);
        when(playerService.getPlayer(any())).thenReturn(losePlayer);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, 2L, 5, LEVEL0.getType(), MOVE.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndAddOrders(ordersDTO, losePlayer.getId()));
    }

    @Test
    void validateAndAddOrders_PlayerAlreadyIssuedOrderTest() {
        PlayerEntity losePlayer = new PlayerEntity();
        losePlayer.setStatus(PlayerStatusEnum.PLAYING);
        losePlayer.setId(1L);
        List<OrderEntity> orders = new ArrayList<>();
        orders.add(new OrderEntity());
        when(repository.findByPlayer(any())).thenReturn(orders);
        when(playerService.getPlayer(any())).thenReturn(losePlayer);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, 2L, 5, LEVEL0.getType(), MOVE.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndAddOrders(ordersDTO, losePlayer.getId()));
    }

    @Test
    void validateAndAddOrders_GameEndedIssueOrderTest() {
        GameEntity game = new GameEntity();
        game.setStatus(GameStatusEnum.ENDED);
        PlayerEntity player = new PlayerEntity();
        player.setStatus(PlayerStatusEnum.PLAYING);
        player.setId(1L);
        player.setGame(game);
        when(playerService.getPlayer(any())).thenReturn(player);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, 2L, 5, LEVEL0.getType(), MOVE.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndAddOrders(ordersDTO, player.getId()));
    }

    @Test
    void validateAndAddOrders_OtherOrdersDuringInitaliseRoundTest() {
        GameEntity game = new GameEntity();
        game.setStatus(GameStatusEnum.PLAYING);
        game.setRoundNo(0);
        PlayerEntity player = new PlayerEntity();
        player.setStatus(PlayerStatusEnum.PLAYING);
        player.setId(1L);
        player.setGame(game);
        when(playerService.getPlayer(any())).thenReturn(player);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, 1L, 5, LEVEL0.getType(), MOVE.getValue()));
        orderDTOS.add(new OrderDTO(1L, 1L, 5, LEVEL0.getType(), ATTACK.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndAddOrders(ordersDTO, player.getId()));
    }

    @Test
    void validateAndAddOrders_OrdersContainInvalidTerritoryTest() throws IllegalAccessException {
        TerritoryEntity territory = new TerritoryEntity();
        territory.setId(1L);
        MapEntity map = new MapEntity();
        map.getTerritories().add(territory);
        GameEntity game = new GameEntity();
        game.setMap(map);
        game.setStatus(GameStatusEnum.PLAYING);
        game.setRoundNo(1);
        PlayerEntity player = new PlayerEntity();
        player.setStatus(PlayerStatusEnum.PLAYING);
        player.setId(1L);
        player.setGame(game);
        when(playerService.getPlayer(any())).thenReturn(player);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(5L, 1L, 5, LEVEL0.getType(), MOVE.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        assertThrows(NoSuchElementException.class, () -> service.validateAndAddOrders(ordersDTO, player.getId()));
    }

    @Test
    void validateAndAddOrders_OrdersContainInvalidDestinationTerritoryTest() {
        TerritoryEntity territory = new TerritoryEntity();
        territory.setId(1L);
        MapEntity map = new MapEntity();
        map.getTerritories().add(territory);
        GameEntity game = new GameEntity();
        game.setMap(map);
        game.setStatus(GameStatusEnum.PLAYING);
        game.setRoundNo(1);
        PlayerEntity player = new PlayerEntity();
        player.setStatus(PlayerStatusEnum.PLAYING);
        player.setId(1L);
        player.setGame(game);
        when(playerService.getPlayer(any())).thenReturn(player);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, 5L, 5, LEVEL0.getType(), MOVE.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        assertThrows(NoSuchElementException.class, () -> service.validateAndAddOrders(ordersDTO, player.getId()));
    }

    @Test
    void validateAndAddOrders_OrdersContainInvalidOrderTypeTest() throws IllegalAccessException {
        TerritoryEntity territory = new TerritoryEntity();
        territory.setId(1L);
        MapEntity map = new MapEntity();
        map.getTerritories().add(territory);
        GameEntity game = new GameEntity();
        game.setMap(map);
        game.setStatus(GameStatusEnum.PLAYING);
        game.setRoundNo(1);
        PlayerEntity player = new PlayerEntity();
        player.setStatus(PlayerStatusEnum.PLAYING);
        player.setId(1L);
        player.setGame(game);
        when(playerService.getPlayer(any())).thenReturn(player);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, 1L, 5, LEVEL0.getType(), "Invalid"));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndAddOrders(ordersDTO, player.getId()));
    }

    @Test
    void validateAndAddOrders_OrdersContain2TechUpgradeOrderTest() throws IllegalAccessException {
        TerritoryEntity territory = new TerritoryEntity();
        territory.setId(1L);
        MapEntity map = new MapEntity();
        map.getTerritories().add(territory);
        GameEntity game = new GameEntity();
        game.setMap(map);
        game.setStatus(GameStatusEnum.PLAYING);
        game.setRoundNo(1);
        PlayerEntity player = new PlayerEntity();
        player.setStatus(PlayerStatusEnum.PLAYING);
        player.setId(1L);
        player.setGame(game);
        when(playerService.getPlayer(any())).thenReturn(player);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, null, 0, null, TECH_RESEARCH.getValue()));
        orderDTOS.add(new OrderDTO(1L, null, 0, null, TECH_RESEARCH.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        assertThrows(IllegalArgumentException.class, () -> service.validateAndAddOrders(ordersDTO, player.getId()));
    }

    @Test
    void validateAndAddOrders_SuccessButNoReadyForRoundExecutionTest() throws IllegalAccessException {
        TerritoryEntity territory = new TerritoryEntity();
        territory.setId(1L);
        MapEntity map = new MapEntity();
        map.getTerritories().add(territory);
        GameEntity game = new GameEntity();
        game.setMap(map);
        game.setStatus(GameStatusEnum.PLAYING);
        game.setRoundNo(1);
        PlayerEntity player = new PlayerEntity();
        player.setStatus(PlayerStatusEnum.PLAYING);
        player.setId(1L);
        player.setGame(game);
        game.getPlayers().add(player);


        when(playerService.getPlayer(any())).thenReturn(player);
        when(repository.findByPlayer(player)).thenReturn(Collections.emptyList());
        when(gameService.getGame(any())).thenReturn(game);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, 1L, 5, LEVEL0.getType(), MOVE.getValue()));
        orderDTOS.add(new OrderDTO(1L, 1L, 5, LEVEL0.getType(), ATTACK.getValue()));
        orderDTOS.add(new OrderDTO(1L, null, 5, LEVEL0.getType(), UNIT_UPGRADE.getValue()));
        orderDTOS.add(new OrderDTO(1L, null, 0, null, TECH_RESEARCH.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        service.validateAndAddOrders(ordersDTO, player.getId());
        verify(repository, times(4)).save(any(OrderEntity.class));
        verify(eventPublisher, times(0)).publishEvent(any(Long.class));
    }

    @Test
    void validateAndAddOrders_SuccessAndReadyForRoundExecutionTest() throws IllegalAccessException {
        TerritoryEntity territory = new TerritoryEntity();
        territory.setId(1L);
        MapEntity map = new MapEntity();
        map.getTerritories().add(territory);
        GameEntity game = new GameEntity();
        game.setMap(map);
        game.setStatus(GameStatusEnum.PLAYING);
        game.setRoundNo(1);
        game.setId(1L);

        PlayerEntity player = new PlayerEntity();
        player.setStatus(PlayerStatusEnum.PLAYING);
        player.setId(1L);
        player.setGame(game);
        game.getPlayers().add(player);

        PlayerEntity loosePlayer = new PlayerEntity();
        loosePlayer.setStatus(PlayerStatusEnum.LOSE);
        loosePlayer.setId(2L);
        loosePlayer.setGame(game);
        game.getPlayers().add(loosePlayer);

        List<OrderEntity> orderEntities_0 = new ArrayList<>();
        List<OrderEntity> orderEntities_1 = new ArrayList<>();
        orderEntities_1.add(new OrderEntity());
        when(playerService.getPlayer(any())).thenReturn(player);
        when(repository.findByPlayer(player)).thenReturn(orderEntities_0).thenReturn(orderEntities_1);
        when(gameService.getGame(any())).thenReturn(game);

        List<OrderDTO> orderDTOS = new ArrayList<>();
        orderDTOS.add(new OrderDTO(1L, 1L, 5, LEVEL0.getType(), MOVE.getValue()));
        orderDTOS.add(new OrderDTO(1L, 1L, 5, LEVEL0.getType(), ATTACK.getValue()));
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderDTOS);

        service.validateAndAddOrders(ordersDTO, player.getId());
        verify(repository, times(2)).save(any(OrderEntity.class));
        verify(eventPublisher, times(1)).publishEvent(any(Long.class));

    }
}
