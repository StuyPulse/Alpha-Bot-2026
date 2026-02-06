package com.stuypulse.robot.subsystems.hoodedshooter;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import edu.wpi.first.math.geometry.Rotation2d;

public class HoodedShooterImpl extends HoodedShooter{
    private final TalonFX hoodMotor;

    private final TalonFX shooterLeader;
    private final TalonFX shooterFollower1;
    // private final TalonFX shooterFollower2;

    public HoodedShooterImpl() {
        super();
        
        hoodMotor = new TalonFX(Ports.HoodedShooter.Hood.HoodMotor);
        shooterLeader = new TalonFX(Ports.HoodedShooter.Shooter.ShooterMotorLead);
        shooterFollower1 = new TalonFX(Ports.HoodedShooter.Shooter.ShooterMotorFollower1);
        // shooterFollower2 = new TalonFX(Ports.HoodedShooter.Shooter.ShooterMotorFollower2);

        Motors.HoodedShooter.Hood.HOOD_CONFIG.configure(hoodMotor);
        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterLeader);
        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterFollower1);
        // Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterFollower2);
    }

    @Override
    public double getCurrentRPS(){
        return shooterLeader.getVelocity().getValueAsDouble();
    }

    @Override
    public Rotation2d getCurrentAngle() {
        return Rotation2d.fromDegrees(hoodMotor.getPosition().getValueAsDouble());
    }

    @Override 
    public void periodic() {
        super.periodic();

        hoodMotor.setControl(new PositionVoltage(getTargetAngle().getRotations()));

        shooterLeader.setControl(new VelocityVoltage(getTargetRPM() / 60));
        shooterFollower1.setControl(new Follower(shooterLeader.getDeviceID(), MotorAlignmentValue.Aligned)); //TODO: MAKE SURE ITS ALIGNED (NONE ARE ROTATING OPPOSITE)
        // shooterFollower2.setControl(new Follower(shooterLeader.getDeviceID(), MotorAlignmentValue.Aligned));
    }
}