package edu.duke.ece651.team13.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An enumeration that represents different levels of units in the game.
 */
@AllArgsConstructor
@Getter
public enum UnitMappingEnum {
    LEVEL0(0, 0, 0, "Basic"),
    LEVEL1(1, 1, 3, "Infantry"),
    LEVEL2(2, 3, 11, "Cavalry"),
    LEVEL3(3, 5, 30, "Artillery"),
    LEVEL4(4, 8, 55, "Army Aviation"),
    LEVEL5(5, 11, 90, "Special Forces"),
    LEVEL6(6, 15, 140, "Combat Engineer"),
    SPY(1, 0, 100, "SPY");

    private final int level;
    private final int bonus;
    private final int cost; //the cost to upgrade unit from level0 to the target level
    private final String type;

    /**
     * Returns the UnitMappingEnum instance that corresponds to the given type value.
     * @param value the unit type value
     * @return the UnitMappingEnum instance that corresponds to the given type value
     * @throws IllegalArgumentException if the given type value is invalid
     */
    public static UnitMappingEnum findByValue(String value) {
        for (UnitMappingEnum unit : values()) {
            if (unit.getType().equals(value)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("The Unit Type mentioned is invalid");
    }

    /**
     * Returns the next level of unit after the given level.
     * @param level the current level of unit
     * @return the next level of unit after the given level
     */
    public static UnitMappingEnum getNextLevel(UnitMappingEnum level) {
        switch (level) {
            case LEVEL0:
                return LEVEL1;
            case LEVEL1:
                return LEVEL2;
            case LEVEL2:
                return LEVEL3;
            case LEVEL3:
                return LEVEL4;
            case LEVEL4:
                return LEVEL5;
            default:
                return LEVEL6;
        }
    }
}
