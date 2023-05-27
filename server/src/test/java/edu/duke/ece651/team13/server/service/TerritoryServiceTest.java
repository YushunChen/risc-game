package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.MapEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.repository.TerritoryConnectionRepository;
import edu.duke.ece651.team13.server.repository.TerritoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.duke.ece651.team13.server.MockDataUtil.getMapEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getPlayerEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getTerritoryEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TerritoryServiceTest {

    private TerritoryService service; //service under test

    @Mock
    private TerritoryRepository repository;

    @Mock
    private TerritoryConnectionRepository neighbourMappingRepository;

    @BeforeEach
    void setUp() {
        service = new TerritoryServiceImpl(repository, neighbourMappingRepository);
    }

    @Test
    void getTerritoryTest() {
        TerritoryEntity territory = getTerritoryEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(territory));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        TerritoryEntity actual = service.getTerritoriesById(1L);
        assertEquals(territory, actual);
        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);

        assertThrows(NoSuchElementException.class, () -> service.getTerritoriesById(2L));
    }

    @Test
    void getTerritoryByOwnerTest() {
        List<TerritoryEntity> territoryEntityList = new ArrayList<>();
        territoryEntityList.add(getTerritoryEntity());
        territoryEntityList.add(getTerritoryEntity());


        when(repository.findByOwner(any())).thenReturn(territoryEntityList);


        List<TerritoryEntity> actual = service.getTerritoriesByPlayer(new PlayerEntity("red"));

        assertEquals(territoryEntityList, actual);
        verify(repository, times(1)).findByOwner(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void createTerritoryTest() {
        MapEntity map = getMapEntity();
        PlayerEntity player = getPlayerEntity();
        TerritoryEntity territory = getTerritoryEntity();
        when(repository.save(any(TerritoryEntity.class))).thenReturn(territory);
        TerritoryEntity actual = service.createTerritory(territory.getName(), map, player, 50, 50);
        assertEquals(territory, actual);
        verify(repository, times(1)).save(any(TerritoryEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateTerritoryTest() {
        PlayerEntity player = getPlayerEntity();
        TerritoryEntity territory = getTerritoryEntity();
        when(repository.save(any(TerritoryEntity.class))).thenReturn(territory);

        TerritoryEntity actual = service.updateTerritoryOwner(territory, player);
        assertEquals(territory, actual);

        verify(repository, times(1)).save(any(TerritoryEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void addNeighbourTest() {
        TerritoryEntity territory = getTerritoryEntity();
        when(neighbourMappingRepository.save(any(TerritoryConnectionEntity.class))).thenReturn(new TerritoryConnectionEntity());

        service.addNeighbour(territory, territory, 5);

        verify(neighbourMappingRepository, times(2)).save(any(TerritoryConnectionEntity.class));
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(neighbourMappingRepository);
    }

    @Test
    void updateTerritoryUnitsTest() {

        TerritoryEntity territory = getTerritoryEntity();
        when(repository.save(any(TerritoryEntity.class))).thenReturn(territory);


        TerritoryEntity actual = service.updateTerritoryUnits(territory, new ArrayList<>());
        assertEquals(territory, actual);

        verify(repository, times(1)).save(any(TerritoryEntity.class));
        verifyNoMoreInteractions(repository);
    }
}
