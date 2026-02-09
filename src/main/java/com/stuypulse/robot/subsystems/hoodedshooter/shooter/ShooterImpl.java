package com.stuypulse.robot.subsystems.hoodedshooter.shooter;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;

public class ShooterImpl extends Shooter {
    private final TalonFX shooterLeader;
    private final TalonFX shooterFollower;

    // Controllers
    private final VelocityVoltage shooterController;
    private final Follower follower;

    public ShooterImpl() {

        shooterController = new VelocityVoltage(getTargetRPM() / 60.0);
        follower = new Follower(Ports.HoodedShooter.Shooter.MOTOR_LEAD, MotorAlignmentValue.Opposed);

        shooterLeader = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_LEAD);
        shooterFollower = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_FOLLOW);
        shooterFollower.setControl(follower);


        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterLeader);
        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterFollower);
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
        super.periodic();

        if (!EnabledSubsystems.HOODED_SHOOTER.get() || getState() == ShooterState.STOP) {
            shooterLeader.stopMotor();
        } else {
            shooterLeader.setControl(shooterController.withVelocity(getTargetRPM() / 60.0));
            shooterFollower.setControl(follower);
        }
        
        if (Settings.DEBUG_MODE) {
        }
    }
}