package com.stuypulse.robot.subsystems.intake;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{
    private static Intake instance; //major access modifier/data type errors (1.)

    static {
        instance = new IntakeImpl();
    }

    public static Intake getInstance() { //(2.)
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

    protected IntakeState state;

    public Intake() {
        state = IntakeState.INTAKING;
    }

    public IntakeState getState() {
        return state;
    }

    public void setState(IntakeState state) {
        this.state = state;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Intake/State", state.toString()); //was changing with errors, but I noticed values didn't change
        //SmartDashboard.putString("States/Intake", state.toString());
    }
}

