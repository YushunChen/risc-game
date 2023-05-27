package edu.duke.ece651.team13.server.entity;

import edu.duke.ece651.team13.server.enums.UnitMappingEnum;
import org.junit.jupiter.api.Test;

import static edu.duke.ece651.team13.server.MockDataUtil.getPlayerEntity;
import static edu.duke.ece651.team13.server.MockDataUtil.getTerritoryEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AttackerEntityTest {

    private final String testName = "testPlayer";

    @Test
    void test_Constructor() {
        TerritoryEntity territory = getTerritoryEntity();
        PlayerEntity player = getPlayerEntity();
        AttackerEntity attacker = new AttackerEntity(territory, player, UnitMappingEnum.LEVEL0, 5);
        assertEquals(territory, attacker.getTerritory());
        assertEquals(player, attacker.getAttacker());
    }

}
