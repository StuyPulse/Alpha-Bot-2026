/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.feeder;

import com.stuypulse.robot.constants.Gains;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;
import com.stuypulse.robot.util.SysId;
import com.stuypulse.stuylib.streams.booleans.BStream;
import com.stuypulse.stuylib.streams.booleans.filters.BDebounce;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import java.util.Optional;

public class FeederImpl extends Feeder {
    private final TalonFX motor;

    private Optional<Double> voltageOverride;
    private final VelocityVoltage controller;

    private BStream isStalling;

    protected FeederImpl() {
        super();
        
        motor = new TalonFX(Ports.Feeder.MOTOR);
        Motors.Feeder.FEEDER_MOTOR_CONFIG.configure(motor);

        voltageOverride = Optional.empty();
        controller = new VelocityVoltage(getTargetRPM() / 60.0);

        isStalling =  BStream.create(() -> motor.getSupplyCurrent().getValueAsDouble() > Settings.Feeder.FEED_STALL_CURRENT)
            .filtered(new BDebounce.Falling(0.8))
            .filtered(new BDebounce.Rising(0.2));
        
    }

    @Override
    public double getRPM() {
        return motor.getVelocity().getValueAsDouble() * 60.0;
    }

    @Override 
    public boolean isStalling() {
        return isStalling.get();
    }

    @Override
    public void periodic() {
        super.periodic();

        Motors.Feeder.FEEDER_MOTOR_CONFIG.updateGainsConfig(
            motor, 
            0, 
            Gains.Feeder.kP, 
            Gains.Feeder.kI,
            Gains.Feeder.kD,
            Gains.Feeder.kS,
            Gains.Feeder.kV,
            Gains.Feeder.kA
        );

        if (EnabledSubsystems.FEEDER.get()) {
            if (getState() == FeederState.STOP) {
                motor.stopMotor();
            } else if (voltageOverride.isPresent()) {
                motor.setVoltage(voltageOverride.get());
            } else if (isStalling()) {
                motor.setControl(controller.withVelocity(FeederState.REVERSE.getTargetRPM() / 60.0).withEnableFOC(true));
            } else {
                motor.setControl(controller.withVelocity(getState().getTargetRPM() / 60.0).withEnableFOC(true));
            }
            
            }

            if (Settings.DEBUG_MODE) {
                SmartDashboard.putBoolean("Feeder/Is Stalling?", isStalling());
                SmartDashboard.putNumber("Feeder/Current (amps)", motor.getStatorCurrent().getValueAsDouble());
                SmartDashboard.putNumber("Feeder/Voltage", motor.getMotorVoltage().getValueAsDouble());
                SmartDashboard.putNumber("Feeder/Supply Current", motor.getSupplyCurrent().getValueAsDouble());
                SmartDashboard.putNumber("Feeder/TESTING KP", Motors.Feeder.FEEDER_MOTOR_CONFIG.getConfiguration().Slot0.kP);
                SmartDashboard.putNumber("Feeder/TESTING KI", Motors.Feeder.FEEDER_MOTOR_CONFIG.getConfiguration().Slot0.kI);
                SmartDashboard.putNumber("Feeder/TESTING KD", Motors.Feeder.FEEDER_MOTOR_CONFIG.getConfiguration().Slot0.kD);
            }
        }

    private void setVoltageOverride(Optional<Double> volts) {
        this.voltageOverride = volts;
    }

    @Override
    public SysIdRoutine getSysIdRoutine() {
        return SysId.getRoutine(
            1, 
            5, 
            "Feeder", 
            voltage -> setVoltageOverride(Optional.of(voltage)), 
            () -> motor.getPosition().getValueAsDouble(), 
            () -> motor.getVelocity().getValueAsDouble(), 
            () -> motor.getMotorVoltage().getValueAsDouble(), 
            getInstance()
        );
    }
}