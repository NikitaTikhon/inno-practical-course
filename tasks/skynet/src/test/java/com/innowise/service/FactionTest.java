package com.innowise.service;

import com.innowise.model.RobotPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Unit tests for the Faction class.
 */
class FactionTest {

    private Faction faction;

    @BeforeEach
    void setUp() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        Factory factory = new Factory(cyclicBarrier, 1);
        faction = new Faction("TestFaction", factory, cyclicBarrier, 1);
    }

    @Test
    @DisplayName("Should have 0 robots when no parts are collected")
    void calculateAssembledRobotsNoParts() {
        assertEquals(0, faction.calculateAssembledRobots());
    }

    @Test
    @DisplayName("Should be able to assemble exactly 1 robot")
    void calculateAssembledRobotsSufficientPartsForOneRobot() {
        addPartsForFaction(faction, RobotPart.HEAD, 1);
        addPartsForFaction(faction, RobotPart.TORSO, 1);
        addPartsForFaction(faction, RobotPart.HAND, 2);
        addPartsForFaction(faction, RobotPart.FEET, 2);

        assertEquals(1, faction.calculateAssembledRobots());
    }

    @Test
    @DisplayName("The number of robots should be limited by the minimum available component set")
    void calculateAssembledRobotsSufficientPartsForMultipleRobots() {
        addPartsForFaction(faction, RobotPart.HEAD, 5);
        addPartsForFaction(faction, RobotPart.TORSO, 4);
        addPartsForFaction(faction, RobotPart.HAND, 9);
        addPartsForFaction(faction, RobotPart.FEET, 12);

        assertEquals(4, faction.calculateAssembledRobots());
    }

    @Test
    @DisplayName("HANDS should be the bottleneck, allowing only 1 robot")
    void calculateAssembledRobotsHandBottleneck() {
        addPartsForFaction(faction, RobotPart.HEAD, 10);
        addPartsForFaction(faction, RobotPart.TORSO, 10);
        addPartsForFaction(faction, RobotPart.HAND, 3);
        addPartsForFaction(faction, RobotPart.FEET, 20);

        assertEquals(1, faction.calculateAssembledRobots());
    }

    @Test
    @DisplayName("FEET should be the bottleneck, allowing 0 robots")
    void calculateAssembledRobotsFEETBottleneck() {
        addPartsForFaction(faction, RobotPart.HEAD, 10);
        addPartsForFaction(faction, RobotPart.TORSO, 10);
        addPartsForFaction(faction, RobotPart.HAND, 20);
        addPartsForFaction(faction, RobotPart.FEET, 1);

        assertEquals(0, faction.calculateAssembledRobots());
    }

    @Test
    @DisplayName("Should be 0 robots if there are no HEADS")
    void calculateAssembledRobots_HeadIsZero() {
        addPartsForFaction(faction, RobotPart.TORSO, 10);
        addPartsForFaction(faction, RobotPart.HAND, 20);
        addPartsForFaction(faction, RobotPart.FEET, 20);

        assertEquals(0, faction.calculateAssembledRobots());
    }

    void addPartsForFaction(Faction faction, RobotPart part, int count) {
        Map<RobotPart, Integer> collectedParts = faction.getCollectedParts();

        collectedParts.put(part, collectedParts.getOrDefault(part, 0) + count);
    }

}
