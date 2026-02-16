/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.feeder;

import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;
import com.stuypulse.robot.util.SysId;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import java.util.Optional;

public class FeederImpl extends Feeder {
    private final TalonFX motor;

    private Optional<Double> voltageOverride;
    private final VelocityVoltage controller;

    protected FeederImpl() {
        super();
        
        motor = new TalonFX(Ports.Feeder.MOTOR);
        Motors.Feeder.FEEDER_MOTOR_CONFIG.configure(motor);

        voltageOverride = Optional.empty();
        controller = new VelocityVoltage(getTargetRPM() / 60.0);
    }

    @Override
    public double getRPM() {
        return motor.getVelocity().getValueAsDouble() * 60.0;
    }

    @Override
    public void periodic() {
        super.periodic();

        if (EnabledSubsystems.FEEDER.get()) {
            if (getState() == FeederState.STOP) {
                motor.stopMotor();
            } else if (voltageOverride.isPresent()) {
                motor.setVoltage(voltageOverride.get());
            } else {
                motor.setControl(controller.withVelocity(getState().getTargetRPM() / 60.0));
            }
        }

        if (Settings.DEBUG_MODE) {
            SmartDashboard.putNumber("Feeder/Current (amps)", motor.getStatorCurrent().getValueAsDouble());
            SmartDashboard.putNumber("Feeder/Voltage", motor.getMotorVoltage().getValueAsDouble());
            SmartDashboard.putNumber("Feeder/Supply Current", motor.getSupplyCurrent().getValueAsDouble());
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