package edu.duke.ece651.team13.server.enums;

import org.junit.jupiter.api.Test;

import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.ATTACK;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.DONE;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.MOVE;
import static edu.duke.ece651.team13.server.enums.OrderMappingEnum.findByValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderMappingEnumTest {

    @Test
    public void getValue_test() {
        assertEquals("MOVE", MOVE.getValue());
        assertEquals("ATTACK", ATTACK.getValue());
        assertEquals("DONE", DONE.getValue());
    }

    @Test
    public void findByValue_test() {
        assertEquals(MOVE, findByValue("MOVE"));
        assertEquals(ATTACK, findByValue("ATTACK"));
        assertEquals(DONE, findByValue("DONE"));
    }
}
