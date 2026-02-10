package com.stuypulse.robot.subsystems.feeder;

import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public abstract class Feeder extends SubsystemBase{
    private static final Feeder instance;
    private FeederState state;

    static {
        instance = new FeederImpl();
    }

    public static Feeder getInstance() {
        return instance;
    }

    public Feeder() {
        state = FeederState.STOP;
    }

    public enum FeederState {
        STOP(Settings.Feeder.FEEDER_STOP), 
        REVERSE(Settings.Feeder.FEEDER_REVERSE), // to unjam the feeder; speed is max, but in reverse
        MAX(Settings.Feeder.FEED_RPM.get());

        private double targetRPM;
        
        private FeederState(double targetRPM) {
            this.targetRPM = targetRPM;
        }

        public double getTargetRPM() {
            return this.targetRPM;
        }
    }

    public void setState(FeederState state) {
        this.state = state;
    }

    public FeederState getState() {
        return state;
    }

    public double getTargetRPM() {
        return getState().getTargetRPM();
    }

    public abstract SysIdRoutine getSysIdRoutine();

    public void periodic() {
        SmartDashboard.putString("Feeder/State", getState().name());
        SmartDashboard.putString("States/Feeder", getState().name());
    }
}
