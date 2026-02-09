package com.stuypulse.robot.subsystems.spindexer;

import java.util.Optional;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;
import com.stuypulse.robot.util.SysId;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class SpindexerImpl extends Spindexer {
    
    private final TalonFX leaderMotor;
    private final TalonFX followerMotor;

    private final VelocityVoltage controller;
    private final Follower follower;

    private Optional<Double> voltageOverride;

    public SpindexerImpl() {
        controller = new VelocityVoltage(state.getTargetRPM() / 60.0);
        follower = new Follower(Ports.Spindexer.MOTOR_LEADER, MotorAlignmentValue.Aligned);

        leaderMotor = new TalonFX(Ports.Spindexer.MOTOR_LEADER);
        followerMotor = new TalonFX(Ports.Spindexer.MOTOR_FOLLOW);

        Motors.Spindexer.spindexerMotors.configure(leaderMotor);
        Motors.Spindexer.spindexerMotors.configure(followerMotor);
        
        followerMotor.setControl(follower);

        voltageOverride = Optional.empty();
    }
    
    private double getRPM() {
        return leaderMotor.getVelocity().getValueAsDouble() * 60.0; // RPS -> RPM
    }
    
    @Override
    public SysIdRoutine getSysIdRoutine() {
        return SysId.getRoutine(
            1, 
            5, 
            "Spindexer", 
            voltage -> setVoltageOverride(Optional.of(voltage)), 
            () -> leaderMotor.getPosition().getValueAsDouble(), 
            () -> leaderMotor.getVelocity().getValueAsDouble(), 
            () -> leaderMotor.getMotorVoltage().getValueAsDouble(), 
            getInstance()
        );
    }

    private void setVoltageOverride(Optional<Double> volts) {
        this.voltageOverride = volts;
    }

    @Override
    public void periodic() {
        super.periodic();

        if (EnabledSubsystems.SPINDEXER.get()) {
            if(!voltageOverride.isEmpty()) {
                leaderMotor.setVoltage(this.voltageOverride.get());
                followerMotor.setControl(follower);
            }
            else {
                leaderMotor.setControl(controller.withVelocity(state.getTargetRPM() / 60.0));
                followerMotor.setControl(follower);
            }
        } else {
            leaderMotor.stopMotor();
            followerMotor.stopMotor();
        }

        SmartDashboard.putNumber("Spindexer/Target RPM", state.getTargetRPM());
        SmartDashboard.putNumber("Spindexer/RPM", getRPM());

        SmartDashboard.putNumber("Spindexer/Leader Current (amps)", leaderMotor.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Leader Voltage", leaderMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Leader Supply Current", leaderMotor.getSupplyCurrent().getValueAsDouble());

        SmartDashboard.putNumber("Spindexer/Follower Current (amps)", followerMotor.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Follower Voltage", followerMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Spindexer/Follower Supply Current", followerMotor.getSupplyCurrent().getValueAsDouble());
    }

}
