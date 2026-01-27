package com.stuypulse.robot.subsystems.hoodedshooter;

import java.util.function.Supplier;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.AngleRPMPair;
import com.stuypulse.robot.util.InterpolationUtil;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class HoodedShooter extends SubsystemBase {
    private HoodedShooterState state;

    public static HoodedShooter instance;

    static {
        instance = new HoodedShooterImpl();
    }

    public static HoodedShooter getinstance() {
        return instance;
    }

    public enum HoodedShooterState {
        SHOOT(
            () -> {
                return InterpolationUtil.getHoodAngleInterpolation(getDistanceFromHub());
            }
        ),
        STOW(
            () -> {
                return new AngleRPMPair(0.0 , new Rotation2d());
            }
        ),
        FERRY    (
            () -> {
                return new AngleRPMPair(0.0 , new Rotation2d());
            }
        );

        private Supplier<AngleRPMPair> angleRPMPair;

        private HoodedShooterState(Supplier<AngleRPMPair> shooterRPM) {
            this.angleRPMPair = shooterRPM;
        }

        public Supplier<AngleRPMPair> getAngleRPMPair() {
            return angleRPMPair;
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

    public static double getDistanceFromHub() {
        return 0.0;
    }

    public abstract double getLeaderRPM();

    public abstract double getFollowerRPM();

    public abstract Rotation2d getHoodAngle();

    public abstract double getShooterRPM();

    public abstract double getLeaderVoltage();

    public abstract double getFollowerVoltage();

    public abstract Rotation2d getTargetAngle();
}
