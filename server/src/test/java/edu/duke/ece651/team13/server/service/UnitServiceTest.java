package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.repository.UnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static edu.duke.ece651.team13.server.MockDataUtil.getTerritoryEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getUnitEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitServiceTest {

    private UnitService service;

    @Mock
    private UnitRepository repository;

    @BeforeEach
    void setUp() {
        service = new UnitServiceImpl(repository);
    }

    @Test
    void createUnitTest() {
        TerritoryEntity territory = getTerritoryEntity();
        UnitEntity unit = getUnitEntity(10);
        when(repository.save(any(UnitEntity.class))).thenReturn(unit);
        UnitEntity actual = service.createUnit(UnitMappingEnum.LEVEL0, territory, 1);
        assertEquals(unit, actual);
        verify(repository, times(1)).save(any(UnitEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateUnitTest() {
        TerritoryEntity territory = getTerritoryEntity();
        UnitEntity unit = getUnitEntity(10);
        when(repository.save(any(UnitEntity.class))).thenReturn(unit);
        UnitEntity actual = service.updateUnit(unit, 5);
        assertEquals(unit, actual);
        verify(repository, times(1)).save(any(UnitEntity.class));
        verifyNoMoreInteractions(repository);
    }
}