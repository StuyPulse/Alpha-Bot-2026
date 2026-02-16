package com.stuypulse.robot.subsystems.hoodedshooter.hood;

import java.util.Optional;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;
import com.stuypulse.robot.util.SysId;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class HoodImpl extends Hood {
    private final TalonFX hoodMotor;
    private final CANcoder hoodEncoder;

    private final PositionVoltage controller;
    private Optional<Double> voltageOverride;
    private boolean seededEnc;

    public HoodImpl() {
        hoodMotor = new TalonFX(Ports.HoodedShooter.Hood.MOTOR);
        hoodEncoder = new CANcoder(Ports.HoodedShooter.Hood.THROUGHBORE_ENCODER);

        Motors.HoodedShooter.Hood.HOOD_CONFIG.configure(hoodMotor);

        hoodMotor.getConfigurator().apply(Motors.HoodedShooter.Hood.hoodSoftwareLimitSwitchConfigs);
        hoodEncoder.getConfigurator().apply(Motors.HoodedShooter.Hood.HOOD_ENCODER);

        controller = new PositionVoltage(getTargetAngle().getRotations());

        hoodMotor.setPosition(hoodEncoder.getAbsolutePosition().getValue());

        voltageOverride = Optional.empty();
    }

    @Override
    public Rotation2d getHoodAngle() {
        return Rotation2d.fromRotations(hoodMotor.getPosition().getValueAsDouble());
    }

    @Override
    public SysIdRoutine getHoodSysIdRoutine() {
        return SysId.getRoutine(
            .45, 
            2, 
            "Hood", 
            voltage -> setVoltageOverride(Optional.of(voltage)), 
            () -> hoodMotor.getPosition().getValueAsDouble(), 
            () -> hoodMotor.getVelocity().getValueAsDouble(), 
            () -> hoodMotor.getMotorVoltage().getValueAsDouble(), 
            getInstance()
        );
    }

    public void setVoltageOverride(Optional<Double> voltageOverride) {
        this.voltageOverride = voltageOverride;
    }

    @Override 
    public void periodic() {
        super.periodic();

        if(!seededEnc) { 
            hoodMotor.setPosition(hoodEncoder.getAbsolutePosition().getValueAsDouble() / Constants.HoodedShooter.Hood.SENSOR_TO_HOOD_RATIO);
            seededEnc = true;
        }

        if (EnabledSubsystems.HOOD.get() && getState() != HoodState.STOW) {
            if (voltageOverride.isPresent()) {
                hoodMotor.setVoltage(voltageOverride.get());
            } else {
                hoodMotor.setControl(controller.withPosition(getTargetAngle().getRotations()));
            }
        } else {
            hoodMotor.stopMotor();
        }

        if (Settings.DEBUG_MODE) {
            SmartDashboard.putNumber("HoodedShooter/Hood/Hood Absolute Angle (deg)", hoodEncoder.getPosition().getValueAsDouble() * 360.0 / Constants.HoodedShooter.Hood.SENSOR_TO_HOOD_RATIO); //* 360.0 / (360.0/35.0) / .97);
            SmartDashboard.putNumber("HoodedShooter/Hood/Input Voltage", hoodMotor.getMotorVoltage().getValueAsDouble());
        }
    }
}