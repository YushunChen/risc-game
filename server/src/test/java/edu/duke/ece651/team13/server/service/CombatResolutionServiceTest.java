package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.entity.AttackerEntity;
import edu.duke.ece651.team13.server.entity.PlayerEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import edu.duke.ece651.team13.server.entity.UnitEntity;
import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import edu.duke.ece651.team13.server.util.Dice;
import org.apache.commons.lang3.tuple.MutablePair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CombatResolutionServiceTest {

    private CombatResolutionServiceImpl service; //service under test

    @Mock
    private AttackerService attackerService;

    @Mock
    private TerritoryService territoryService;

    @Mock
    private UnitService unitService;

    @Mock
    private Dice dice;

    @BeforeEach
    void setUp() {
        service = new CombatResolutionServiceImpl(attackerService, territoryService, unitService, dice);
    }


    @Test
    void reduceUnitTest() {
        List<MutablePair<UnitMappingEnum, Integer>> units = new ArrayList<>();

        units.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 2));
        units.add(new MutablePair<>(UnitMappingEnum.LEVEL1, 2));

        service.reduceUnit(units, UnitMappingEnum.LEVEL0);

        assertEquals(units.size(), 2);
        assertEquals(units.get(0).getLeft(), UnitMappingEnum.LEVEL0);
        assertEquals(units.get(0).getRight(), Integer.valueOf(1));

        service.reduceUnit(units, UnitMappingEnum.LEVEL0);

        assertEquals(units.size(), 1);
        assertEquals(units.get(0).getLeft(), UnitMappingEnum.LEVEL1);
    }

    @Test
    void fightTest_attackerWin() {
        when(dice.roll()).thenReturn(1, 15);

        List<MutablePair<UnitMappingEnum, Integer>> attacker = new ArrayList<>();

        attacker.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));
        attacker.add(new MutablePair<>(UnitMappingEnum.LEVEL6, 1));

        List<MutablePair<UnitMappingEnum, Integer>> defender = new ArrayList<>();

        defender.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));
        defender.add(new MutablePair<>(UnitMappingEnum.LEVEL6, 1));

        service.fight(attacker, defender, false, false);

        assertEquals(attacker.size(), 2);
        assertEquals(defender.size(), 1);
        assertEquals(defender.get(0).getLeft(), UnitMappingEnum.LEVEL6);
    }

    @Test
    void fightTest_defenderWin() {
        when(dice.roll()).thenReturn(1, 3);

        List<MutablePair<UnitMappingEnum, Integer>> attacker = new ArrayList<>();

        attacker.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));
        attacker.add(new MutablePair<>(UnitMappingEnum.LEVEL1, 1));

        List<MutablePair<UnitMappingEnum, Integer>> defender = new ArrayList<>();

        defender.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));
        defender.add(new MutablePair<>(UnitMappingEnum.LEVEL6, 1));

        service.fight(attacker, defender, false, false);

        assertEquals(1, attacker.size());
        assertEquals(attacker.get(0).getLeft(), UnitMappingEnum.LEVEL0);
        assertEquals(2, defender.size());
    }

    @Test
    void fightTest_tieattackerownerWin() {
        when(dice.roll()).thenReturn(1, 1);

        List<MutablePair<UnitMappingEnum, Integer>> attacker = new ArrayList<>();

        attacker.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));

        List<MutablePair<UnitMappingEnum, Integer>> defender = new ArrayList<>();

        defender.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));

        service.fight(attacker, defender, true, false);

        assertEquals(1, attacker.size());
        assertEquals(0, defender.size());
    }

    @Test
    void fightTest_tiedefenderownerWin() {
        when(dice.roll()).thenReturn(1, 1);

        List<MutablePair<UnitMappingEnum, Integer>> attacker = new ArrayList<>();

        attacker.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));

        List<MutablePair<UnitMappingEnum, Integer>> defender = new ArrayList<>();

        defender.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));

        service.fight(attacker, defender, false, true);

        assertEquals(0, attacker.size());
        assertEquals(1, defender.size());
    }

    @Test
    void fightTest_tiecontinue() {
        when(dice.roll()).thenReturn(1, 1, 2, 1);

        List<MutablePair<UnitMappingEnum, Integer>> attacker = new ArrayList<>();

        attacker.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));

        List<MutablePair<UnitMappingEnum, Integer>> defender = new ArrayList<>();

        defender.add(new MutablePair<>(UnitMappingEnum.LEVEL0, 1));

        service.fight(attacker, defender, false, false);

        assertEquals(1, attacker.size());
        assertEquals(0, defender.size());
    }

    @Test
    void getWarPartiesTest() {
        PlayerEntity red = new PlayerEntity("red");
        PlayerEntity blue = new PlayerEntity("blue");
        PlayerEntity green = new PlayerEntity("green");

        TerritoryEntity territory = new TerritoryEntity();
        territory.setOwner(red);
        territory.getUnits().add(new UnitEntity(1L, UnitMappingEnum.LEVEL0, territory, 5));
        List<AttackerEntity> attackerEntities = new ArrayList<>();
        attackerEntities.add(new AttackerEntity(territory, blue, UnitMappingEnum.LEVEL1, 2));
        attackerEntities.add(new AttackerEntity(territory, blue, UnitMappingEnum.LEVEL2, 1));
        attackerEntities.add(new AttackerEntity(territory, green, UnitMappingEnum.LEVEL3, 1));

        Map<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> actual = service.getWarParties(attackerEntities, territory);

        assertEquals(1, actual.get(red).size());
        assertEquals(2, actual.get(blue).size());
        assertEquals(1, actual.get(green).size());
    }

    @Test
    void resolveWinner_defenderWinTest() {
        when(dice.roll()).thenReturn(0);
        PlayerEntity defender = new PlayerEntity("defender");
        PlayerEntity attacker = new PlayerEntity("attacker");
        defender.setId(1L);
        attacker.setId(2L);

        TerritoryEntity territory = new TerritoryEntity();
        territory.setOwner(defender);
        territory.getUnits().add(new UnitEntity(1L, UnitMappingEnum.LEVEL5, territory, 1));
        List<AttackerEntity> attackerEntities = new ArrayList<>();
        attackerEntities.add(new AttackerEntity(territory, attacker, UnitMappingEnum.LEVEL0, 1));

        Map.Entry<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> actual = service.resolveWinner(territory, attackerEntities);

        assertEquals(defender, actual.getKey());
    }

    @Test
    void resolveWinner_attackerWinTest() {

        when(dice.roll()).thenReturn(0);
        PlayerEntity defender = new PlayerEntity("defender");
        PlayerEntity attacker = new PlayerEntity("attacker");
        defender.setId(1L);
        attacker.setId(2L);

        TerritoryEntity territory = new TerritoryEntity();
        territory.setOwner(defender);
        territory.getUnits().add(new UnitEntity(1L, UnitMappingEnum.LEVEL0, territory, 1));
        List<AttackerEntity> attackerEntities = new ArrayList<>();
        attackerEntities.add(new AttackerEntity(territory, attacker, UnitMappingEnum.LEVEL5, 1));

        Map.Entry<PlayerEntity, List<MutablePair<UnitMappingEnum, Integer>>> actual = service.resolveWinner(territory, attackerEntities);

        assertEquals(attacker, actual.getKey());
    }

    @Test
    void resolveCombat_NoAttacker() {
        PlayerEntity defender = new PlayerEntity("defender");

        defender.setId(1L);


        TerritoryEntity territory = new TerritoryEntity();
        territory.setOwner(defender);
        territory.getUnits().add(new UnitEntity(1L, UnitMappingEnum.LEVEL0, territory, 1));

        when(attackerService.getAttackers(territory)).thenReturn(new ArrayList<>());

        service.resolveCombot(territory);
        verify(territoryService, times(0)).updateTerritoryUnits(any(TerritoryEntity.class), any());
        verify(territoryService, times(0)).updateTerritoryOwner(any(TerritoryEntity.class), any());
        verify(attackerService, times(0)).clearAttackers(any(TerritoryEntity.class));
    }

    @Test
    void resolveCombat() {
        PlayerEntity defender = new PlayerEntity("defender");
        PlayerEntity attacker = new PlayerEntity("attacker");
        defender.setId(1L);
        attacker.setId(2L);

        TerritoryEntity territory = new TerritoryEntity();
        territory.setOwner(defender);
        territory.getUnits().add(new UnitEntity(1L, UnitMappingEnum.LEVEL0, territory, 1));
        List<AttackerEntity> attackerEntities = new ArrayList<>();
        attackerEntities.add(new AttackerEntity(territory, attacker, UnitMappingEnum.LEVEL0, 1));

        when(dice.roll()).thenReturn(2, 1);
        when(attackerService.getAttackers(territory)).thenReturn(attackerEntities);

        service.resolveCombot(territory);
        verify(territoryService, times(1)).updateTerritoryUnits(any(TerritoryEntity.class), any());
        verify(territoryService, times(1)).updateTerritoryOwner(any(TerritoryEntity.class), any());
        verify(attackerService, times(1)).clearAttackers(any(TerritoryEntity.class));
    }

    @Test
    void addUnitsToMutablePairListTest() {
        List<MutablePair<UnitMappingEnum, Integer>> units = new ArrayList<>();



        service.addUnitsToMutablePairList(units, UnitMappingEnum.LEVEL0, 5);

        assertEquals(1, units.size());
        assertEquals(UnitMappingEnum.LEVEL0, units.get(0).getLeft() );
        assertEquals(5, units.get(0).getRight() );

        service.addUnitsToMutablePairList(units, UnitMappingEnum.LEVEL1, 2);

        assertEquals(2, units.size());
        assertEquals(UnitMappingEnum.LEVEL1, units.get(1).getLeft() );
        assertEquals(2, units.get(1).getRight() );

        service.addUnitsToMutablePairList(units, UnitMappingEnum.LEVEL1, 2);

        assertEquals(2, units.size());
        assertEquals(UnitMappingEnum.LEVEL1, units.get(1).getLeft() );
        assertEquals(4, units.get(1).getRight() );
    }

}
