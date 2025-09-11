package com.innowise.service;

import com.innowise.model.RobotPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Factory class.
 */
class FactoryTest {

    private Factory factory;

    @BeforeEach
    void setUp() {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        factory = new Factory(cyclicBarrier, 1);
    }

    @Test
    @DisplayName("Should take no parts if storage is empty")
    void takePartsStorageIsEmptyReturnsEmptyList() {
        List<RobotPart> takenParts = factory.takeParts(5);

        assertTrue(takenParts.isEmpty());
        assertEquals(0, getStorageSize(factory));
    }

    @Test
    @DisplayName("Should take up to the specified maximum amount of parts")
    void takePartsStorageHasEnoughPartsReturnsCorrectAmountAndReducesStorage() {
        addPartForFactory(factory, RobotPart.HEAD);
        addPartForFactory(factory, RobotPart.TORSO);
        addPartForFactory(factory, RobotPart.HAND);
        addPartForFactory(factory, RobotPart.FEET);

        List<RobotPart> takenParts = factory.takeParts(2);
        assertEquals(2, takenParts.size());
        assertEquals(2, getStorageSize(factory));
    }

    @Test
    @DisplayName("Should take all remaining parts if the requested amount is more than what's available")
    void takePartsStorageHasFewerPartsThanRequestedReturnsAllRemaining() {
        addPartForFactory(factory, RobotPart.HEAD);
        addPartForFactory(factory, RobotPart.TORSO);

        List<RobotPart> takenParts = factory.takeParts(5);
        assertEquals(2, takenParts.size());
        assertEquals(0, getStorageSize(factory));
    }

    void addPartForFactory(Factory factory, RobotPart part) {
        BlockingDeque<RobotPart> storage = factory.getStorage();
        
        storage.add(part);
    }
    
    int getStorageSize(Factory factory) {
       return factory.getStorage().size();
    }
    
}
