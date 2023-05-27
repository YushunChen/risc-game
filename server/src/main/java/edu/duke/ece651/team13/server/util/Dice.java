package edu.duke.ece651.team13.server.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * This class represents the dice
 */
@NoArgsConstructor
@Component
public class Dice {
    private final int minValue = 1;
    private final int maxValue = 20;

    /**
     * Roll the dice
     *
     * @return the result ranged in minvalue-maxvalue, included
     */
    public int roll() {
        Random rand = new Random();
        return rand.nextInt((maxValue - minValue) + 1) + minValue;
    }
}
