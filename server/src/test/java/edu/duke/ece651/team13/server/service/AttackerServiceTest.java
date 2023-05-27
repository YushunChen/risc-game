package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.AttackerEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.repository.AttackerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static edu.duke.ece651.team13.server.MockDataUtil.getAttackerEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getPlayerEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getTerritoryEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AttackerServiceTest {

    private AttackerService service; //service under test

    @Mock
    private AttackerRepository repository;

    @BeforeEach
    void setUp() {
        service = new AttackerServiceImpl(repository);
    }

    @Test
    void getAttackerTest() {
        TerritoryEntity territory = getTerritoryEntity();
        List<AttackerEntity> attackers = new ArrayList<>();
        attackers.add(getAttackerEntity(territory));

        when(repository.findByTerritory(territory)).thenReturn(attackers);


        List<AttackerEntity> actual = service.getAttackers(territory);
        assertEquals(attackers, actual);
        verify(repository, times(1)).findByTerritory(territory);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void addAttackerTest() {
        TerritoryEntity territory = getTerritoryEntity();
        PlayerEntity player = getPlayerEntity();
        AttackerEntity attacker = getAttackerEntity(territory);

        when(repository.save(any(AttackerEntity.class))).thenReturn(attacker);

        AttackerEntity actual = service.addAttacker(territory, player, UnitMappingEnum.LEVEL0, 5);
        assertEquals(attacker, actual);
        verify(repository, times(1)).save(any(AttackerEntity.class));
        verifyNoMoreInteractions(repository);
    }


    @Test
    void deleteAttackerTest() {
        TerritoryEntity territory = getTerritoryEntity();

        service.clearAttackers(territory);

        verify(repository, times(1)).deleteByTerritory(any(TerritoryEntity.class));
        verifyNoMoreInteractions(repository);
    }
}
