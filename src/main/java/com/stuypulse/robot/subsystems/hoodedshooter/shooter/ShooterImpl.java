package com.stuypulse.robot.subsystems.hoodedshooter.shooter;

import java.util.Optional;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;
import com.stuypulse.robot.util.SysId;

import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class ShooterImpl extends Shooter {
    private final TalonFX shooterLeader;
    private final TalonFX shooterFollower;

    // Controllers
    private final VelocityVoltage shooterController;
    private final Follower follower;

    private Optional<Double> shooterVoltageOverride;

    public ShooterImpl() {

        shooterController = new VelocityVoltage(getTargetRPM() / 60.0);
        follower = new Follower(Ports.HoodedShooter.Shooter.MOTOR_LEAD, MotorAlignmentValue.Opposed);

        shooterLeader = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_LEAD);
        shooterFollower = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_FOLLOW);
        shooterFollower.setControl(follower);

        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterLeader);
        Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterFollower);

        shooterVoltageOverride = Optional.empty();
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

    public void setVoltageOverride(Optional<Double> shooterVoltageOverride) {
        this.shooterVoltageOverride = shooterVoltageOverride;
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

    @Override 
    public void periodic() {
        super.periodic();

        if (shooterVoltageOverride.isPresent()) {
            shooterLeader.setVoltage(shooterVoltageOverride.get());
            shooterFollower.setControl(follower);
        }
        else if (!EnabledSubsystems.SHOOTER.get() || getState() == ShooterState.STOP) {
            shooterLeader.stopMotor();
        } else {
            shooterLeader.setControl(shooterController.withVelocity(getTargetRPM() / 60.0));
            shooterFollower.setControl(follower);
        }
        
        if (Settings.DEBUG_MODE) {
        }
    }
}