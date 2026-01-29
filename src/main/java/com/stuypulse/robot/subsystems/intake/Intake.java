package com.stuypulse.robot.subsystems.intake;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{
    private static IntakeImpl instance;

    static {
        instance = new IntakeImpl();
    }

    public static IntakeImpl getInstance() {
        return instance;
    }

    public enum IntakeState {
        STOP(Settings.Intake.STOP),
        INTAKING(Settings.Intake.INTAKE_SPEED),
        OUTTAKING(Settings.Intake.OUTTAKE_SPEED);
        
        private final double speed;
        
        private IntakeState(double speed){
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }
    }

    private IntakeState state;

    public Intake() {
        state = IntakeState.STOP;
    }

    public IntakeState getState() {
        return state;
    }

    public void setState(IntakeState state) {
        this.state = state;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Intake/State", state.toString());
    }
}

