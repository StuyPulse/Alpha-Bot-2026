package com.stuypulse.robot.subsystems.spindexer;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Spindexer extends SubsystemBase {

    public static final Spindexer instance;

    static {
        instance = new SpindexerImpl();
    }

    public static Spindexer getInstance() {
        return instance;
    }

    public enum SpindexerState {
        RUNNING(Settings.Spindexer.RUNNING_SPEED.doubleValue()),
        STOP(0);

        private double spindexer_speed;

        private SpindexerState(double spindexer_speed) {
            this.spindexer_speed = spindexer_speed;
        }

        public double getSpindexerSpeed() {
            return this.spindexer_speed;
        }
    }

    protected SpindexerState state;

    protected Spindexer() {
        this.state = SpindexerState.RUNNING;
    }

    public SpindexerState getState() {
        return state;
    }

    public void setState(SpindexerState state) {
        this.state = state;
    }
    
}
