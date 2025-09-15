package com.innowise.simulation;

import com.innowise.service.Faction;
import com.innowise.service.Factory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.CyclicBarrier;

/**
 * The RobotWarsSimulator class for running the robot wars simulation.
 * It creates the threads for the factory and factions,
 * starts them, and reports the final results.
 */
public class RobotWarSimulator {

    private static final Logger LOGGER = LogManager.getLogger(RobotWarSimulator.class.getName());
    private static final int SIMULATION_DAYS = 100;
    private static final int PARTICIPANTS = 3;

    private RobotWarSimulator() {}

    /**
     * The startSimulation entry point for the simulation.
     */
    public static void startSimulation() {
        if (SIMULATION_DAYS != 100) {
            throw new IllegalArgumentException("The simulation must run for exactly 100 days.");
        }

        LOGGER.info("Simulation starting! Duration: " + SIMULATION_DAYS + " days.");

        CyclicBarrier barrier = new CyclicBarrier(PARTICIPANTS);

        Factory factory = new Factory(barrier, SIMULATION_DAYS);
        Faction world = new Faction("World", factory, barrier, SIMULATION_DAYS);
        Faction wednesday = new Faction("Wednesday", factory, barrier, SIMULATION_DAYS);

        Thread factoryThread = new Thread(factory);
        Thread worldThread = new Thread(world);
        Thread wednesdayThread = new Thread(wednesday);

        factoryThread.start();
        worldThread.start();
        wednesdayThread.start();

        try {
            factoryThread.join();
            worldThread.join();
            wednesdayThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Main simulation thread was interrupted.");
        }

        LOGGER.info("\n=============================================");
        LOGGER.info("Simulation finished! Final Results:");
        LOGGER.info("=============================================");

        world.printFinalStats();
        wednesday.printFinalStats();

        int worldRobots = world.calculateAssembledRobots();
        int wednesdayRobots = wednesday.calculateAssembledRobots();

        LOGGER.info("\n--- FINAL VERDICT ---");
        if (worldRobots > wednesdayRobots) {
            LOGGER.info("Faction '{}' wins with an army of {} robots versus {}!\n",
                    world.getName(), worldRobots, wednesdayRobots);
        } else if (wednesdayRobots > worldRobots) {
            LOGGER.info("Faction '{}' wins with an army of {} robots versus {}!\n",
                    wednesday.getName(), wednesdayRobots, worldRobots);
        } else {
            LOGGER.info("It's a draw! Both factions assembled {} robots.\n", worldRobots);
        }
    }

}
