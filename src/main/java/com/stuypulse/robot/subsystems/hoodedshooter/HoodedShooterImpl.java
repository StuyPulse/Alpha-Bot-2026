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
        shooterMotorLead = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_LEAD);
        Motors.HoodedShooter.Shooter.MOTOR_LEADER_CONFIG.configure(shooterMotorLead);

        shooterMotorFollower = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_FOLLOW);
        Motors.HoodedShooter.Shooter.MOTOR_FOLLOW_CONFIG.configure(shooterMotorFollower);

        hoodMotor = new TalonFX(Ports.HoodedShooter.Hood.MOTOR);
    }

    public double getTargetRPM() {
        return getState().getAngleRPMPair().get().getDouble();
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
        // return (getLeaderRPM() + getFollowerRPM()) / 2.0;
        return getLeaderRPM(); // Technically, the RPM we care about is just the leader motor's
    }

    @Override 
    public Rotation2d getTargetAngle() {
        return getState().getAngleRPMPair().get().getAngle();
    }

    @Override
    public void periodic() {
        shooterMotorLead.setControl(new VelocityVoltage(getTargetRPM() / 60.0));
        shooterMotorFollower.setControl(new Follower(Ports.HoodedShooter.Shooter.MOTOR_LEAD, MotorAlignmentValue.Opposed));

        hoodMotor.setControl(new PositionVoltage(getTargetAngle().getDegrees()));

        // SHOOTER
        SmartDashboard.putNumber("HoodedShooter/Shooter/Lead RPM", getLeaderRPM());
        SmartDashboard.putNumber("HooderShooter/Shooter/Follower RPM", getFollowerRPM());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Shooter Average RPM", getShooterRPM());

        SmartDashboard.putNumber("HooderShooter/Shooter/Lead Voltage", shooterMotorLead.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("HooderShooter/Shooter/Lead Voltage", shooterMotorLead.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", shooterMotorFollower.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", shooterMotorFollower.getStatorCurrent().getValueAsDouble());

        // HOOD
        SmartDashboard.putNumber("HoodedShooter/Hood/Hood Target Angle (deg)", getTargetAngle().getDegrees());
        SmartDashboard.putNumber("HoodedShooter/Hood/Hood Current Angle (deg)", getHoodAngle().getDegrees());

        SmartDashboard.putNumber("HoodedShooter/Hood/Hood Voltage", hoodMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("HoodedShooter/Hood/Hood Current", hoodMotor.getStatorCurrent().getValueAsDouble());

    }

}
