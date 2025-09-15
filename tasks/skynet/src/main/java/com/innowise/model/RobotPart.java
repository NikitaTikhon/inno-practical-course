package com.innowise.model;

public enum RobotPart {

    HEAD, TORSO, HAND, FEET;

    private static final RobotPart[] parts = RobotPart.values();

    public static RobotPart[] getRobotParts() {
        return parts;
    }

}
