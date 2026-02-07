package com.stuypulse.robot.subsystems.feeder;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase{
    private static final Feeder instance;
    private FeederState state;

    static {
        instance = new FeederImpl();
    }

    public static Feeder getInstance() {
        return instance;
    }

    protected Feeder() {
        state = FeederState.MAX;
    }

    public enum FeederState {
        STOP(Settings.Feeder.FEEDER_STOP), 
        REVERSE(Settings.Feeder.FEEDER_REVERSE), // to unjam the feeder; speed is max, but in reverse
        MAX(Settings.Feeder.FEEDER_MAX);

        private double dutyCycle;
        
        private FeederState(double speed) {
            this.dutyCycle = speed;
        }

        public double getTargetDutyCycle() {
            return this.dutyCycle;
        }
    }

    public void setFeederState(FeederState state) {
        this.state = state;
    }

    public FeederState getFeederState() {
        return state;
    }

    public void periodic() {
        SmartDashboard.putString("Feeder/State", getFeederState().name());
        SmartDashboard.putString("States/Feeder", getFeederState().name());
    }
}
