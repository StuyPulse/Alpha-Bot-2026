package com.stuypulse.robot.subsystems.intake;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Motors;

// .setControl (new Follower()); //because we have two motors on the intake
// one motor needs to be inverted
public class IntakeImpl extends Intake {
    private final TalonFX intakeMotor1;
    private final TalonFX intakeMotor2;

    public IntakeImpl() {
        intakeMotor1 = new TalonFX(0);
        intakeMotor2 = new TalonFX(1); //TODO: fill in actual ports

        Motors.Intake.intakeMotor1.configure(intakeMotor1);
        Motors.Intake.intakeMotor2.configure(intakeMotor2);
    }

    @Override
    public void periodic() {
        intakeMotor2.setControl(new Follower(0, null)); //TODO: fill in later
    
        intakeMotor1.setControl(new VelocityVoltage(getState().getVoltage()));
    }
    
}