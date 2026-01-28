package com.stuypulse.robot.subsystems.hoodedshooter;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;

public class HoodedShooterImpl extends HoodedShooter {
    private final TalonFX shooterMotorLead;
    private final TalonFX shooterMotorFollower;
    private final TalonFX hoodMotor;

    public HoodedShooterImpl() {
        shooterMotorLead = new TalonFX(Ports.HoodedShooter.Shooter.ShooterMotorLead);
        Motors.HoodedShooter.Shooter.SHOOTER_MASTER_CONFIG.configure(shooterMotorLead);

        shooterMotorFollower = new TalonFX(Ports.HoodedShooter.Shooter.ShooterMotorFollower);
        Motors.HoodedShooter.Shooter.SHOOTER_FOLLOWER_CONFIG.configure(shooterMotorFollower);

        hoodMotor = new TalonFX(Ports.HoodedShooter.Hood.HoodMotor);
    }

    public double getTargetRPM() {
        return getState().getAngleRPMPair().get().getdouble();
    }

    @Override
    public double getLeaderRPM() {
        return shooterMotorLead.getVelocity().getValueAsDouble() * 60.0;
    }

    @Override
    public double getFollowerRPM() {
        return shooterMotorFollower.getVelocity().getValueAsDouble() * 60.0;
    }

    @Override
    public Rotation2d getHoodAngle() {
        return Rotation2d.fromRotations(hoodMotor.getPosition().getValueAsDouble());
    }

    @Override
    public double getShooterRPM() {
        return (getLeaderRPM() + getFollowerRPM()) / 2.0;
    }

    @Override
    public double getLeaderVoltage() {
        return shooterMotorLead.getMotorVoltage().getValueAsDouble();
    }

    @Override
    public double getFollowerVoltage() {
        return shooterMotorFollower.getMotorVoltage().getValueAsDouble();
    }

    @Override 
    public Rotation2d getTargetAngle() {
        return getState().getAngleRPMPair().get().getAngle();
    }

    @Override
    public void periodic() {
        shooterMotorLead.setControl(new VelocityVoltage(getTargetRPM() / 60.0));
        shooterMotorFollower.setControl(new Follower(Ports.HoodedShooter.Shooter.ShooterMotorLead, MotorAlignmentValue.Opposed));

        hoodMotor.setControl(new PositionVoltage(getTargetAngle().getDegrees()));

        SmartDashboard.putNumber("HoodedShooter/Shooter/Lead RPM", getFollowerRPM());
        SmartDashboard.putNumber("HooderShooter/Shooter/Follower RPM", getFollowerRPM());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Shooter Average RPM", getShooterRPM());
        SmartDashboard.putNumber("HooderShooter/Shooter/Lead Voltage", getLeaderRPM());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Lead Voltage", getLeaderVoltage());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", getFollowerVoltage());
    }

}
