package com.innowise.service;

import com.innowise.model.RobotPart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * The Factory class, which produces parts. It runs in its own thread.
 * This class acts as the "Producer" in the simulation.
 */
public class Factory implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Factory.class.getName());

    /**
     * The storage for parts. Using LinkedBlockingDeque as it's a thread-safe
     * collection suitable for producer-consumer scenarios.
     */
    private final BlockingDeque<RobotPart> storage = new LinkedBlockingDeque<>();
    private final Random random = new Random();
    private final CyclicBarrier barrier;
    private final int simulationDays;

    /**
     * Constructs a new Factory.
     *
     * @param barrier        The CyclicBarrier used to synchronize simulation steps (day/night).
     * @param simulationDays The total number of days the simulation will run.
     */
    public Factory(CyclicBarrier barrier, int simulationDays) {
        this.barrier = barrier;
        this.simulationDays = simulationDays;
    }

    @Override
    public void run() {
        for (int day = 1; day <= simulationDays; day++) {
            try {
                produce(day);

                barrier.await();

                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Factory thread was interrupted.");
            }
        }
    }

    /**
     * Produces a random number of random parts (between 1 and 10).
     *
     * @param day The current simulation day, used for logging.
     */
    private void produce(int day) {
        List<RobotPart> producedToday = new ArrayList<>();
        int partsToProduce = random.nextInt(10) + 1;
        RobotPart[] allRobotParts = RobotPart.getRobotParts();

        for (int i = 0; i < partsToProduce; i++) {
            RobotPart newPart = allRobotParts[random.nextInt(allRobotParts.length)];
            storage.add(newPart);
            producedToday.add(newPart);
        }

        String report = producedToday.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        LOGGER.info("--- Day {} ---\n[FACTORY]: Produced {} parts: [{}]. In storage: {}\n",
                day, partsToProduce, report, storage.size());
    }

    /**
     * A thread-safe method for factions to take parts from the storage.
     * Factions call this method during the night phase.
     *
     * @param maxAmount The maximum number of parts a faction can carry.
     * @return A list of parts taken from the storage.
     */
    public List<RobotPart> takeParts(int maxAmount) {
        List<RobotPart> takenParts = new ArrayList<>();

        for (int i = 0; i < maxAmount; i++) {
            RobotPart part = storage.pollLast();
            if (part != null) {
                takenParts.add(part);
            } else {
                break;
            }
        }
        return takenParts;
    }

    public BlockingDeque<RobotPart> getStorage() {
        return storage;
    }

}
