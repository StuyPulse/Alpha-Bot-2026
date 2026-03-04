/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.hoodedshooter.shooter;

import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;
import com.stuypulse.robot.util.SysId;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Time;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import java.util.Optional;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

public class ShooterImpl extends Shooter {
    private final TalonFX shooterLeader;
    private final TalonFX shooterFollower;

    // Controllers
    private final VelocityVoltage shooterController;
    private final Follower follower;

    private Optional<Double> voltageOverride;
    private boolean hasHitSetpoint;

    private double minBangBangRPM;
    private double maxBangBangRPM;

    //private final BangBangController bangbangController;

    public ShooterImpl() {

        shooterController = new VelocityVoltage(getTargetRPM() / 60.0);
        follower = new Follower(Ports.HoodedShooter.Shooter.MOTOR_LEAD, MotorAlignmentValue.Opposed);

        shooterLeader = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_LEAD);
        shooterFollower = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_FOLLOW);
        shooterFollower.setControl(follower);

        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterLeader);
        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterFollower);

        // FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        // feedbackConfigs.withVelocityFilterTimeConstant(Time.ofBaseUnits(0.1, Units.Seconds));

        // shooterLeader.getConfigurator().apply(feedbackConfigs);
        // TODO: refactor this to be in the motor configs 

        voltageOverride = Optional.empty();

        shooterLeader.getPosition();

        minBangBangRPM = -1;
        maxBangBangRPM = -1;
        //bangbangController = new BangBangController(); 
    }

    @Override
    public double getShooterRPM() {
        return getLeaderRPM();
    }

    public double getLeaderRPM() {
        return shooterLeader.getVelocity().getValueAsDouble() * 60.0;
    }

    public double getFollowerRPM() {
        return shooterFollower.getVelocity().getValueAsDouble() * 60.0;
    }


    public double calculateBangBang(double measurement, double setpoint) {
        return measurement < setpoint ? 1 : 0; //factor
    }

    public double getAverageBangBangTargetRPM() {
        maxBangBangRPM = getShooterRPM() > maxBangBangRPM && getShooterRPM() > getTargetRPM() - 500 ? getShooterRPM() : maxBangBangRPM;
        minBangBangRPM = getShooterRPM() < minBangBangRPM && getShooterRPM() > getTargetRPM() - 500 ? getShooterRPM() : minBangBangRPM;

        if (maxBangBangRPM < 0) {
            maxBangBangRPM = getShooterRPM();
        }
        if (minBangBangRPM < 0) {
            minBangBangRPM = getShooterRPM();
        }

        return (maxBangBangRPM - minBangBangRPM) / 2;
    }

    @Override 
    public void periodic() {
        super.periodic();

        if (getState() == ShooterState.SHOOT && getLeaderRPM() - getTargetRPM() < Settings.HoodedShooter.SHOOTER_TOLERANCE_RPM) {
            hasHitSetpoint = true;
        }

        boolean shouldAddFeedforward = hasHitSetpoint && getLeaderRPM() - getTargetRPM() < -Settings.HoodedShooter.SHOOTER_TOLERANCE_RPM;

        if (EnabledSubsystems.SHOOTER.get()) {
            if (getState() == ShooterState.STOP) {
                shooterLeader.stopMotor();
                shooterFollower.stopMotor();
            } else if (voltageOverride.isPresent()) {

                // shooterLeader.setControl(new DutyCycleOut(calculateBangBang(getLeaderRPM(), getTargetRPM())).withEnableFOC(true));

                // shooterLeader.setVoltage(voltageOverride.get());
                shooterFollower.setControl(follower);
            } //else if (shouldAddFeedforward) {
                // shooterLeader.setControl(shooterController.withVelocity(getTargetRPM() / 60.0).withFeedForward(2.0));
                // shooterFollower.setControl(follower);
              else {
                //ACTUAL:
                shooterLeader.setControl(shooterController.withVelocity(getTargetRPM() / 60.0).withEnableFOC(true));
                // shooterFollower.setControl(follower);

                //BANGBANG:
                // shooterLeader.setControl(new VoltageOut(bangbangController.calculate(getLeaderRPM(), getTargetRPM()) * 12).withEnableFOC(true));
                // shooterFollower.setControl(follower);

                //BANGBANG METHOD (DANIEL WAY):
                // shooterLeader.setControl(new DutyCycleOut(calculateBangBang(getLeaderRPM(), getTargetRPM())).withEnableFOC(true));

                // shooterLeader.setVoltage(voltageOverride.get());
                shooterFollower.setControl(follower);
            }
        } else {
            shooterLeader.stopMotor();
            shooterFollower.stopMotor();
        }
        
        if (Settings.DEBUG_MODE) {
            SmartDashboard.putNumber("HoodedShooter/Shooter/Bang Bang Average", getAverageBangBangTargetRPM());
            SmartDashboard.putNumber("HoodedShooter/Shooter/Bang Bang MAX", minBangBangRPM);
            SmartDashboard.putNumber("HoodedShooter/Shooter/Bang Bang MIN", maxBangBangRPM);

            SmartDashboard.putNumber("HoodedShooter/Shooter/Leader Current (amps)", shooterLeader.getSupplyCurrent().getValueAsDouble());
            SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Current (amps)", shooterFollower.getSupplyCurrent().getValueAsDouble());

            SmartDashboard.putNumber("HoodedShooter/Shooter/Leader Voltage", shooterLeader.getMotorVoltage().getValueAsDouble());
            SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", shooterFollower.getMotorVoltage().getValueAsDouble());

            SmartDashboard.putBoolean("HoodedShooter/Shooter/Should Add Feedforward", shouldAddFeedforward);
            SmartDashboard.putNumber("HoodedShooter/Shooter/Follower RPM", getFollowerRPM());

            SmartDashboard.putNumber("InterpolationTesting/Shooter Closed Loop Error", shooterLeader.getClosedLoopError().getValueAsDouble() * 60.0);
            SmartDashboard.putNumber("InterpolationTesting/Shooter Applied Voltage", shooterLeader.getMotorVoltage().getValueAsDouble());
            SmartDashboard.putNumber("InterpolationTesting/Shooter Supply Current", shooterLeader.getSupplyCurrent().getValueAsDouble());
        }
    }

    public void setVoltageOverride(Optional<Double> voltageOverride) {
        this.voltageOverride = voltageOverride;
    }

    @Override
    public SysIdRoutine getShooterSysIdRoutine() {
        return SysId.getRoutine(
            1, 
            5, 
            "Shooter", 
            voltage -> setVoltageOverride(Optional.of(voltage)), 
            () -> shooterLeader.getPosition().getValueAsDouble(), 
            () -> shooterLeader.getVelocity().getValueAsDouble(), 
            () -> shooterLeader.getMotorVoltage().getValueAsDouble(), 
            getInstance()
        );
    }
}