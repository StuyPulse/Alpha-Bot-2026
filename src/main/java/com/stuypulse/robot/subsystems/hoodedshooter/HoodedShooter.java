package com.stuypulse.robot.subsystems.hoodedshooter;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class HoodedShooter extends SubsystemBase{
    private HoodedShooterState state;
    
    public static HoodedShooter instance;

    static {
        instance = new HoodedShooterImpl();
    }

    public static HoodedShooter getinstance() {
        return instance;
    }

    public enum HoodedShooterState {
        SHOOT(() -> { return 0.0; }, new Rotation2d()),
        STOW(() -> { return 0.0; }, new Rotation2d()),
        FERRY(() -> { return 0.0; }, new Rotation2d());

        private Supplier<Double> shooterRPM;
        private Rotation2d hoodAngle;

        private HoodedShooterState(Supplier<Double> shooterRPM, Rotation2d hoodangle) {
            this.shooterRPM = shooterRPM;
            this.hoodAngle = hoodangle;
        }

        public Supplier<Double> getShooterRPM() {
            return shooterRPM;
        }

        public Rotation2d getHoodAngle() {
            return hoodAngle;
        }
    }

    public HoodedShooter() {
        state = HoodedShooterState.STOW;
    }

    public void setState(HoodedShooterState state) {
        this.state = state;
    }

    public HoodedShooterState getState() {
        return state;
    }
    

    public abstract double getLeaderRPM();
    public abstract double getFollowerRPM();
    public abstract Rotation2d getHoodAngle();
    public abstract double getShooterRPM();
    public abstract double getLeaderVoltage();
    public abstract double getFollowerVoltage();
}
