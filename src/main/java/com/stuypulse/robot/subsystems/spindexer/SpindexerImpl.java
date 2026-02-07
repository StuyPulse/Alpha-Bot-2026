package com.stuypulse.robot.subsystems.spindexer;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SpindexerImpl extends Spindexer {
    
    private final TalonFX leader;
    private final TalonFX follower;

    private final DutyCycleOut spindexerDutyCycleController;

    private final Follower spindexerFollow;

    public SpindexerImpl() {
        leader = new TalonFX(Ports.Spindexer.MOTOR_LEADER);
        follower = new TalonFX(Ports.Spindexer.MOTOR_FOLLOW);

        Motors.Spindexer.spindexerMotors.configure(leader);
        Motors.Spindexer.spindexerMotors.configure(follower);

        spindexerDutyCycleController = new DutyCycleOut(0.0);
        spindexerFollow = new Follower(Ports.Spindexer.MOTOR_LEADER, MotorAlignmentValue.Aligned);
    }

    private double getRPM() {
        return leader.getVelocity().getValueAsDouble() * 60.0; // RPS -> RPM
    }

    @Override
    public void periodic() {
        if (Settings.EnabledSubsystems.SPINDEXER.getAsBoolean()) {
            leader.setControl(spindexerDutyCycleController.withOutput(getState().getTargetSpeed()));
            follower.setControl(spindexerFollow);
        }

        SmartDashboard.putNumber("Spindexer/Target RPM", getState().getTargetSpeed());
        SmartDashboard.putNumber("Spindexer/RPM", getRPM());

        SmartDashboard.putNumber("Spindexer/Leader Current (amps)", leader.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Leader Voltage", leader.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Leader Supply Current", leader.getSupplyCurrent().getValueAsDouble());

        SmartDashboard.putNumber("Spindexer/Follower Current (amps)", follower.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Follower Voltage", follower.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Follower Supply Current", follower.getSupplyCurrent().getValueAsDouble());
    }

}
