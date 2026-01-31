package com.stuypulse.robot.subsystems.hoodedshooter;

import java.util.function.Supplier;

import com.stuypulse.robot.util.AngleRPMPair;
import com.stuypulse.robot.util.InterpolationUtil;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class HoodedShooter extends SubsystemBase {
    private static HoodedShooter instance;
    private HoodedShooterState state;

    static {
        instance = new HoodedShooterImpl();
    }

    public static HoodedShooter getInstance() {
        return instance;
    }

    public enum HoodedShooterState {
        SHOOT(() -> InterpolationUtil.getHoodAngleInterpolation(getDistanceFromHub())),
        STOW(() ->  new AngleRPMPair()),
        FERRY(() -> new AngleRPMPair());

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

    public abstract double getShooterRPM();

    public abstract Rotation2d getTargetAngle();

    public abstract Rotation2d getHoodAngle();

    @Override
    public void periodic() {
        SmartDashboard.putString("HoodedShooter/State", state.name());
        SmartDashboard.putString("States/HoodedShooter", state.name());
    }
}
