package com.innowise.service;


import com.innowise.model.RobotPart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

/**
 * The Faction class, which takes parts from the Factory. It runs in its own thread.
 * This class acts as a "Consumer" in the simulation.
 */
public class Faction implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Faction.class.getName());
    private final String name;
    private final Factory factory;
    private final CyclicBarrier barrier;
    private final int simulationDays;
    private static final int MAX_PARTS_PER_DAY = 5;

    /**
     * Storage for counting collected parts of each type.
     */
    private final Map<RobotPart, Integer> collectedParts = new ConcurrentHashMap<>();

    /**
     * Constructs a new Faction.
     *
     * @param name           The name of the faction.
     * @param factory        The factory instance from which parts will be taken.
     * @param barrier        The CyclicBarrier used to synchronize simulation steps.
     * @param simulationDays The total number of days the simulation will run.
     */
    public Faction(String name, Factory factory, CyclicBarrier barrier, int simulationDays) {
        this.name = name;
        this.factory = factory;
        this.barrier = barrier;
        this.simulationDays = simulationDays;
        for (RobotPart type : RobotPart.getRobotParts()) {
            collectedParts.put(type, 0);
        }
    }

    @Override
    public void run() {
        for (int day = 1; day <= simulationDays; day++) {
            try {
                startDayPhase();
                startNightPhase();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Faction thread %s was interrupted.\n {}", name);
            }
        }
    }

    /**
     * Represents the "Day" phase for the faction.
     * The faction waits on the barrier for the factory to finish producing parts.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting.
     * @throws BrokenBarrierException if the barrier is in a broken state.
     */
    private void startDayPhase() throws InterruptedException, BrokenBarrierException {
        barrier.await();
    }

    /**
     * Represents the "Night" phase for the faction.
     * The faction collects parts and then waits for other factions to finish.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting.
     * @throws BrokenBarrierException if the barrier is in a broken state.
     */
    private void startNightPhase() throws InterruptedException, BrokenBarrierException {
        collectParts();
        barrier.await();
    }

    /**
     * Takes parts from the factory.
     */
    private void collectParts() {
        List<RobotPart> taken = factory.takeParts(MAX_PARTS_PER_DAY);

        if (taken.isEmpty()) {
            LOGGER.info("[{}]: Wanted to take parts, but the storage is empty!\n", name);
        } else {
            for (RobotPart part : taken) {
                collectedParts.put(part, collectedParts.get(part) + 1);
            }
            String report = taken.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            LOGGER.info("[{}]: Collected {} parts: [{}]\n", name, taken.size(), report);
        }
    }

    /**
     * Calculates the number of fully assembled robots.
     * A robot requires: 1 HEAD, 1 TORSO, 2 HANDS, 2 FEET.
     *
     * @return The total number of complete robots that can be assembled.
     */
    public int calculateAssembledRobots() {
        int heads = collectedParts.getOrDefault(RobotPart.HEAD, 0);
        int torsos = collectedParts.getOrDefault(RobotPart.TORSO, 0);
        int hands = collectedParts.getOrDefault(RobotPart.HAND, 0) / 2;
        int feet = collectedParts.getOrDefault(RobotPart.FEET, 0) / 2;
        return Math.min(Math.min(heads, torsos), Math.min(hands, feet));
    }

    /**
     * Gets the name of the faction.
     * @return The faction's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Prints the final statistics for this faction to the console.
     */
    public void printFinalStats() {
        LOGGER.info("\n--- Stats for Faction {} ---\n", name);
        collectedParts.forEach((part, count) -> LOGGER.info("  - {}: {}\n", part, count));
        LOGGER.info("  => Total robots assembled: {}\n", calculateAssembledRobots());
    }

    public Map<RobotPart, Integer> getCollectedParts() {
        return collectedParts;
    }

}

