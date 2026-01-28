package com.stuypulse.robot.subsystems.spindexer;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SpindexerImpl extends Spindexer {
    
    private final TalonFX[] spindexerMotors;

    public SpindexerImpl() {
        super();

        spindexerMotors = new TalonFX[] {
            new TalonFX(Ports.Spindexer.MOTOR_LEAD),
            new TalonFX(Ports.Spindexer.MOTOR_FOLLOW)
        };

        Motors.Spindexer.spindexerMotors.configure(spindexerMotors[0]);
        Motors.Spindexer.spindexerMotors.configure(spindexerMotors[1]);

    }

    private void setMotorsBasedOnState() {
        spindexerMotors[0].setControl(new DutyCycleOut(state.getSpindexerSpeed()));
        spindexerMotors[1].setControl(new Follower(Ports.Spindexer.MOTOR_LEAD, MotorAlignmentValue.Opposed));
    }

    private double getRPM() {
        return spindexerMotors[0].getVelocity().getValueAsDouble() * 60.0; // RPS -> RPM
    }

    @Override
    public void periodic() {
        setMotorsBasedOnState();
        SmartDashboard.putNumber("Spindexer/Cake RPM", getRPM());
    }

}
