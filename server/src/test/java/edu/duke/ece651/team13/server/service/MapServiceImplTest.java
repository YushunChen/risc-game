package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.GameEntity;
import edu.duke.ece651.team13.server.entity.MapEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.repository.MapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.duke.ece651.team13.server.MockDataUtil.getGameEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getMapEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getPlayerEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapServiceImplTest {

    private MapService service;
    @Mock
    private MapRepository repository;
    @Mock
    private TerritoryService territoryService;

    @Mock
    private UnitService unitService;

    @Mock
    private TerritoryViewService territoryViewService;

    @Mock
    private UnitViewService unitViewService;

    @Mock
    private SpyUnitService spyUnitService;

    @BeforeEach
    void setUp() {
        service = new MapServiceImpl(repository, territoryService, unitService, territoryViewService, unitViewService, spyUnitService);
    }

    @Test
    void createMapFor4playersTest() {
        MapEntity map4players = getMapEntity();
        MapEntity actualMap = createMapHelper(map4players, 4);
        assertEquals(map4players, actualMap);
        verify(repository, times(1)).save(any(MapEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createMapFor3playersTest() {
        MapEntity map3players = getMapEntity();
        MapEntity actualMap = createMapHelper(map3players, 3);
        assertEquals(map3players, actualMap);
        verify(repository, times(1)).save(any(MapEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createMapFor2playersTest() {
        MapEntity map2players = getMapEntity();
        MapEntity actualMap = createMapHelper(map2players, 2);
        assertEquals(map2players, actualMap);
        verify(repository, times(1)).save(any(MapEntity.class));
        verifyNoMoreInteractions(repository);
    }

    MapEntity createMapHelper(MapEntity map, int no_players) {
        GameEntity gameEntity = getGameEntity();
        when(repository.save(any(MapEntity.class))).thenReturn(map);
        return service.createMap(gameEntity, getPlayersHelper(no_players));
    }

    List<PlayerEntity> getPlayersHelper(int no_players) {
        List<PlayerEntity> players = new ArrayList<>();
        for (int i = 0; i < no_players; i++) {
            players.add(getPlayerEntity());
        }
        return players;
    }

    @Test
    void getMapTest() {
        MapEntity map = getMapEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(map));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        MapEntity actual = service.getMap(1L);
        assertEquals(map, actual);
        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);

        assertThrows(NoSuchElementException.class, () -> service.getMap(2L));
    }
}