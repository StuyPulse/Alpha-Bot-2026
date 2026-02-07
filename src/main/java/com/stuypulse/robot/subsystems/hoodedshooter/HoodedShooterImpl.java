package com.stuypulse.robot.subsystems.hoodedshooter;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HoodedShooterImpl extends HoodedShooter {
    private final TalonFX shooterLeader;
    private final TalonFX shooterFollower;
    private final TalonFX hoodMotor;
    private final CANcoder hoodEncoder;
    
    private boolean hasSeededHood;

    public HoodedShooterImpl() {
        shooterLeader = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_LEAD);
        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterLeader);

        shooterFollower = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_FOLLOW);
        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterFollower);
        shooterFollower.setControl(new Follower(Ports.HoodedShooter.Shooter.MOTOR_LEAD, MotorAlignmentValue.Opposed));

        hoodMotor = new TalonFX(Ports.HoodedShooter.Hood.MOTOR);
        Motors.HoodedShooter.Hood.HOOD_CONFIG.configure(hoodMotor);

        hoodEncoder = new CANcoder(Ports.HoodedShooter.Hood.ENCODER);
    }

    @Override
    public Rotation2d getHoodAngle() {
        return Rotation2d.fromRotations(hoodMotor.getPosition().getValueAsDouble());
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

    @Override 
    public void periodic() {
        if (!hasSeededHood) {
            hoodMotor.setPosition(hoodEncoder.getPosition().getValueAsDouble());
        }

        shooterLeader.setControl(new VelocityVoltage(getTargetRPM() / 60.0));
        hoodMotor.setControl(new PositionVoltage(getTargetAngle().getRotations()));
        
        // SHOOTER
        SmartDashboard.putNumber("HoodedShooter/Shooter/Leader RPM", getLeaderRPM());
        SmartDashboard.putNumber("HooderShooter/Shooter/Follower RPM", getFollowerRPM());

        SmartDashboard.putNumber("HooderShooter/Shooter/Lead Voltage", shooterLeader.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("HooderShooter/Shooter/Lead Voltage", shooterLeader.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", shooterFollower.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", shooterFollower.getStatorCurrent().getValueAsDouble());

        // HOOD
        SmartDashboard.putNumber("HoodedShooter/Hood/Hood Voltage", hoodMotor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("HoodedShooter/Hood/Hood Current", hoodMotor.getStatorCurrent().getValueAsDouble());
    }
}