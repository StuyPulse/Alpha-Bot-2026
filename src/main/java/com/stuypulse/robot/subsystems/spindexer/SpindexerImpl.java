package com.stuypulse.robot.subsystems.spindexer;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SpindexerImpl extends Spindexer {
    
    private final TalonFX leaderMotor;
    private final TalonFX followerMotor;

    private final VelocityVoltage controller;
    private final Follower follower;

    public SpindexerImpl() {
                
        controller = new VelocityVoltage(getTargetRPM() / 60.0);
        follower = new Follower(Ports.Spindexer.MOTOR_LEADER, MotorAlignmentValue.Aligned);

        leaderMotor = new TalonFX(Ports.Spindexer.MOTOR_LEADER);
        followerMotor = new TalonFX(Ports.Spindexer.MOTOR_FOLLOW);

        Motors.Spindexer.spindexerMotors.configure(leaderMotor);
        Motors.Spindexer.spindexerMotors.configure(followerMotor);
        
        followerMotor.setControl(follower);
    }

    private double getRPM() {
        return leaderMotor.getVelocity().getValueAsDouble() * 60.0; // RPS -> RPM
    }

    @Override
    public void periodic() {
        super.periodic();

        if (EnabledSubsystems.SPINDEXER.get()) {
            leaderMotor.setControl(controller.withVelocity(getTargetRPM() / 60.0));
            followerMotor.setControl(follower);
        } else {
            leaderMotor.stopMotor();
            followerMotor.stopMotor();
        }

        SmartDashboard.putNumber("Spindexer/Target RPM", getTargetRPM());
        SmartDashboard.putNumber("Spindexer/RPM", getRPM());

        SmartDashboard.putNumber("Spindexer/Leader Current (amps)", leaderMotor.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Leader Voltage", leaderMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Leader Supply Current", leaderMotor.getSupplyCurrent().getValueAsDouble());

        SmartDashboard.putNumber("Spindexer/Follower Current (amps)", followerMotor.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Follower Voltage", followerMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Follower Supply Current", followerMotor.getSupplyCurrent().getValueAsDouble());
    }

}
