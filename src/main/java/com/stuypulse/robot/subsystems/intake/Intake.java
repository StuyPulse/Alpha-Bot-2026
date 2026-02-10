package com.stuypulse.robot.subsystems.intake;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{
    private static final Intake instance;
    private IntakeState state;

    static {
        instance = new IntakeImpl();
    }

    public static Intake getInstance() {
        return instance;
    }

    public enum IntakeState {
        STOP(Settings.Intake.STOP),
        INTAKING(Settings.Intake.INTAKE_SPEED),
        OUTTAKING(Settings.Intake.OUTTAKE_SPEED);
        
        private double dutyCycle;
        
        private IntakeState(double dutyCycle){
            this.dutyCycle = dutyCycle;
        }

        public double getDutyCycle() {
            return dutyCycle;
        }
    }

    public Intake() {
        state = IntakeState.STOP;
    }

    public void setState(IntakeState state) {
        this.state = state;
    }

    public IntakeState getState() {
        return state;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Intake/State", getState().name());
        SmartDashboard.putString("States/Intake", getState().name());
    }
}

