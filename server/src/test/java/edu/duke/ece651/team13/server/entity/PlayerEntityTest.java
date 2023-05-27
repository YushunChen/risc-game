package edu.duke.ece651.team13.server.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerEntityTest {

    private final String testName = "testPlayer";

    @Test
    void test_getName() {
        PlayerEntity p = new PlayerEntity(testName);
        assertEquals(testName, p.getName());
    }

}
