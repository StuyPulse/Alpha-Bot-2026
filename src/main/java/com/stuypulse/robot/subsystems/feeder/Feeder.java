package com.stuypulse.robot.subsystems.feeder;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase{
    private static final Feeder instance;
    private SmartNumber speed;

    static {
        instance = new FeederImpl();
    }

    public static Feeder getInstance() {
        return instance;
    }

    public enum FeederState {
        STOP(Settings.Feeder.FEEDER_STOP), 
        REVERSE(Settings.Feeder.FEEDER_REVERSE), // to unjam the feeder; speed is max, but in reverse
        MAX(Settings.Feeder.FEEDER_MAX);

        private double feederSpeed;
        
        private FeederState(double speed) {
            this.feederSpeed = speed;
        }

        public double getFeederSpeed() {
            return this.feederSpeed;
        }
    }

    private FeederState feederState;

    public void setFeederState(FeederState feederState) {
        this.feederState = feederState;
    }

    protected Feeder() {
        this.feederState = FeederState.MAX;
        speed = new SmartNumber("Feeder Speed", Settings.Feeder.FEEDER_MAX);
    }

    public FeederState getFeederState() {
        return this.feederState;
    }

    public void periodic() {
        SmartDashboard.putString("FeederState", getFeederState().toString());
    }
}
