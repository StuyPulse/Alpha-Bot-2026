package com.stuypulse.robot.subsystems.spindexer;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Spindexer extends SubsystemBase {
    private static final Spindexer instance;

    private SpindexerState state;

    static {
        instance = new SpindexerImpl();
    }

    public static Spindexer getInstance() {
        return instance;
    }

    public enum SpindexerState {
        RUNNING(Settings.Spindexer.RUNNING_SPEED),
        STOP(0.0);

        private double targetSpeed;

        private SpindexerState(double targetSpeed) {
            this.targetSpeed = targetSpeed;
        }

        public double getTargetSpeed() {
            return this.targetSpeed;
        }
    }

    public Spindexer() {
        this.state = SpindexerState.STOP;
    }

    public SpindexerState getState() {
        return state;
    }

    public void setState(SpindexerState state) {
        this.state = state;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Spindexer/State", getState().name());
        SmartDashboard.putString("States/Spindexer", getState().name());
    }
    
}
