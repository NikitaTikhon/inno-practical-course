package com.innowise.simulation;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RobotWarSimulatorTest {

    /**
     * A simple test to ensure the simulation runs to completion
     * without throwing any exceptions and produces output.
     */
    @Test
    void testStartSimulationSimulationRunsWithoutErrors() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            assertDoesNotThrow(RobotWarSimulator::startSimulation,
                    "The main simulation method should not throw any exceptions.");

            String output = outContent.toString();
            assertTrue(output.contains("Simulation finished!"), "The simulation output should indicate that it finished.");
            assertTrue(output.contains("FINAL VERDICT"), "The simulation output should contain the final verdict.");
        } finally {
            System.setOut(originalOut);
        }
    }

}