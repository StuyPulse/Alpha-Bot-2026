package com.stuypulse.robot.subsystems.hoodedshooter.shooter;

import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Shooter extends SubsystemBase {
    private static final Shooter instance;

    private ShooterState state;

    static {
        instance = new ShooterImpl();
    }

    public static Shooter getInstance() {
        return instance;
    }

    public enum ShooterState {
        STOP,
        SHOOT,
        FERRY;
    }

    public Shooter() {
        state = ShooterState.STOP;
    }

    public void setState(ShooterState state) {
        this.state = state;
    }

    public ShooterState getState() {
        return state;
    }

    public double getTargetRPM() {
        return switch(state) {
            case STOP -> 0;
            case FERRY -> getFerryRPM();
            case SHOOT -> getShootRPM();
        };
    }

    public double getShootRPM() {
        return Constants.HoodedShooter.SHOT_RPM.get(); // will return different speeds in future based on distance to hub
    }

    public double getFerryRPM() {
        return Constants.HoodedShooter.FERRY_RPM; 
    }

    public boolean atTolerance() {
        double diff = Math.abs(getTargetRPM() - getShooterRPM());
        return diff > Settings.HoodedShooter.SHOOTER_TOLERANCE_RPM;
    }

    public abstract double getShooterRPM();

    @Override
    public void periodic() {
        SmartDashboard.putString("HoodedShooter/State", state.name());
        SmartDashboard.putString("States/HoodedShooter", state.name());
    }
}
