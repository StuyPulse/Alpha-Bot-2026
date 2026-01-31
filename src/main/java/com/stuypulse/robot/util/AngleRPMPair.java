package com.stuypulse.robot.util;

import edu.wpi.first.math.geometry.Rotation2d;

public class AngleRPMPair {
    private final double distance;
    private final Rotation2d angle;

    public AngleRPMPair(double distance, Rotation2d angle) {
        this.distance = distance;
        this.angle = angle;
    }

    public AngleRPMPair() {
        this.distance = 0.0;
        this.angle = Rotation2d.kZero;
    }

    public Rotation2d getAngle() {
        return this.angle;
    }

    public double getDouble() {
        return this.distance;
    }
}
