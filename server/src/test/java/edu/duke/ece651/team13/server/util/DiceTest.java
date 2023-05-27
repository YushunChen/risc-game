package edu.duke.ece651.team13.server.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiceTest {

    private Dice dice; //service under test

    @BeforeEach
    void setUp() {
        dice = new Dice();
    }


    @Test
    void test_roll() {
        int roll = dice.roll();

        assert(roll >= 1 && roll <=20);
    }
}