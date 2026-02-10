package com.stuypulse.robot.subsystems.hoodedshooter.shooter;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

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
            case SHOOT -> getShootRPM();
            case FERRY -> getFerryRPM();
        };
    }

    public double getShootRPM() {
        return Settings.HoodedShooter.SHOOT_RPM.get(); // will return different speeds in future based on distance to hub
    }

    public double getFerryRPM() {
        return Settings.HoodedShooter.FERRY_RPM.get(); 
    }

    public boolean atTolerance() {
        double diff = Math.abs(getTargetRPM() - getShooterRPM());
        return diff > Settings.HoodedShooter.SHOOTER_TOLERANCE_RPM;
    }

    public abstract double getShooterRPM();

    public abstract SysIdRoutine getShooterSysIdRoutine();

    @Override
    public void periodic() {
        SmartDashboard.putString("Shooter/State", state.name());
        SmartDashboard.putString("States/Shooter", state.name());
    }
}
