package com.stuypulse.robot.subsystems.hoodedshooter;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.Follower;

public class HoodedShooterImpl extends HoodedShooter{
    private final TalonFX ShooterMotorLead;
    private final TalonFX ShooterMotorFollower;

    private final TalonFX HoodMotor;    

    public HoodedShooterImpl() {

        ShooterMotorLead = new TalonFX(Ports.HoodedShooter.Shooter.ShooterMotorLead);
        Motors.HoodedShooter.Shooter.SHOOTER_MASTER_CONFIG.configure(ShooterMotorLead);
        
        ShooterMotorFollower = new TalonFX(Ports.HoodedShooter.Shooter.ShooterMotorFollower);
        Motors.HoodedShooter.Shooter.SHOOTER_FOLLOWER_CONFIG.configure(ShooterMotorFollower);

        HoodMotor = new TalonFX(Ports.HoodedShooter.Hood.HoodMotor);
    }

    public double getTargetRPM() {
        return getState().getShooterRPM().get();
    }

    @Override 
    public double getLeaderRPM() {
        return ShooterMotorLead.getVelocity().getValueAsDouble() * 60 ;
    }
    
    @Override
    public double getFollowerRPM() {
        return ShooterMotorFollower.getVelocity().getValueAsDouble() * 60;
    }
    
    @Override 
    public Rotation2d getHoodAngle() {
        return Rotation2d.fromRotations(HoodMotor.getPosition().getValueAsDouble());
    }

    @Override
    public double getShooterRPM() {
        return (getLeaderRPM() + getFollowerRPM()) / 2;
    }

    @Override
    public double getLeaderVoltage() {
        return ShooterMotorLead.getMotorVoltage().getValueAsDouble();
    }

    @Override
    public double getFollowerVoltage() {
        return ShooterMotorFollower.getMotorVoltage().getValueAsDouble();
    }

    @Override
    public void periodic() {
        ShooterMotorLead.setControl(new VelocityVoltage(getTargetRPM() / 60.0));
        ShooterMotorFollower.setControl(new Follower(Ports.HoodedShooter.Shooter.ShooterMotorLead, MotorAlignmentValue.Opposed)); 

        SmartDashboard.putNumber("HoodedShooter/Shooter/Lead RPM", getFollowerRPM());
        SmartDashboard.putNumber("HooderShooter/Shooter/Follower RPM", getFollowerRPM());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Shooter Average RPM", getShooterRPM());
        SmartDashboard.putNumber("HooderShooter/Shooter/Lead Voltage", getLeaderRPM());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Lead Voltage", getLeaderVoltage());
        SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", getFollowerVoltage());
    }
    
}
