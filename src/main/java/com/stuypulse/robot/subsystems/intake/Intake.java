/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
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
