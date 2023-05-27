package edu.duke.ece651.team13.server.enums;

import org.junit.jupiter.api.Test;

import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL0;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL1;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL2;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL3;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL4;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL5;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.LEVEL6;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.findByValue;
import static edu.duke.ece651.team13.server.enums.UnitMappingEnum.getNextLevel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnitMappingEnumTest {

    @Test
    public void getNextLevel_test() {
        assertEquals(LEVEL1, getNextLevel(LEVEL0));
        assertEquals(LEVEL2, getNextLevel(LEVEL1));
        assertEquals(LEVEL3, getNextLevel(LEVEL2));
        assertEquals(LEVEL4, getNextLevel(LEVEL3));
        assertEquals(LEVEL5, getNextLevel(LEVEL4));
        assertEquals(LEVEL6, getNextLevel(LEVEL5));
        assertEquals(LEVEL6, getNextLevel(LEVEL6));
    }

    @Test
    public void findByValue_test() {
        assertEquals(LEVEL0, findByValue("Basic"));
        assertEquals(LEVEL1, findByValue("Infantry"));
        assertEquals(LEVEL2, findByValue("Cavalry"));
        assertEquals(LEVEL3, findByValue("Artillery"));
        assertEquals(LEVEL4, findByValue("Army Aviation"));
        assertEquals(LEVEL5, findByValue("Special Forces"));
        assertEquals(LEVEL6, findByValue("Combat Engineer"));
        assertThrows(IllegalArgumentException.class , () -> findByValue("dummy"));
    }
}
