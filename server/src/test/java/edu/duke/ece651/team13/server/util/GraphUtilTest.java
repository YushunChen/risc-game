package edu.duke.ece651.team13.server.util;

import edu.duke.ece651.team13.server.entity.TerritoryConnectionEntity;
import edu.duke.ece651.team13.server.entity.TerritoryEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static edu.duke.ece651.team13.server.MockDataUtil.getTerritoryEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphUtilTest {

    @Test
    void test_findMinCost() {
        TerritoryEntity t1 = getTerritoryEntity();
        TerritoryEntity t2 = getTerritoryEntity();
        TerritoryEntity t3 = getTerritoryEntity();

        // t1--10--t2
        //  \       |
        //   3--t3--2
        TerritoryConnectionEntity t1t2 = new TerritoryConnectionEntity(t1, t2, 10);
        TerritoryConnectionEntity t2t1 = new TerritoryConnectionEntity(t2, t1, 10);
        TerritoryConnectionEntity t1t3 = new TerritoryConnectionEntity(t1, t3, 3);
        TerritoryConnectionEntity t3t1 = new TerritoryConnectionEntity(t3, t1, 3);
        TerritoryConnectionEntity t2t3 = new TerritoryConnectionEntity(t2, t3, 2);
        TerritoryConnectionEntity t3t2 = new TerritoryConnectionEntity(t3, t2, 2);
        t1.setConnections(Arrays.asList(t1t2, t1t3));
        t2.setConnections(Arrays.asList(t2t1, t2t3));
        t3.setConnections(Arrays.asList(t3t1, t3t2));

        assertEquals(5, GraphUtil.findMinCost(t1, t2));
        assertEquals(5, GraphUtil.findMinCost(t2, t1));
        assertEquals(2, GraphUtil.findMinCost(t3, t2));
        assertEquals(2, GraphUtil.findMinCost(t2, t3));
        assertEquals(3, GraphUtil.findMinCost(t1, t3));
        assertEquals(3, GraphUtil.findMinCost(t3, t1));
    }
}