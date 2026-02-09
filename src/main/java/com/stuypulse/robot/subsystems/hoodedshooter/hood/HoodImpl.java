package com.stuypulse.robot.subsystems.hoodedshooter.hood;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;

import edu.wpi.first.math.geometry.Rotation2d;

public class HoodImpl extends Hood {
    private final TalonFX hoodMotor;
    private final CANcoder hoodEncoder;

    // Controllers
    private final PositionVoltage hoodController;
    
    private boolean hasSeededHood;

    public HoodImpl() {

        hoodController = new PositionVoltage(getTargetAngle().getRotations());

        hoodMotor = new TalonFX(Ports.HoodedShooter.Hood.MOTOR);
        hoodEncoder = new CANcoder(Ports.HoodedShooter.Hood.THROUGHBORE_ENCODER);

        Motors.HoodedShooter.Hood.HOOD_CONFIG.configure(hoodMotor);
    }

    @Override
    public Rotation2d getHoodAngle() {
        return Rotation2d.fromRotations(hoodMotor.getPosition().getValueAsDouble());
    }

    @Override 
    public void periodic() {
        super.periodic();

        if (!hasSeededHood) {
            hoodMotor.setPosition(hoodEncoder.getPosition().getValueAsDouble());
        }

        if (!EnabledSubsystems.HOODED_SHOOTER.get() || getState() == HoodState.STOW) {
            hoodMotor.stopMotor();
        } else {
            hoodMotor.setControl(hoodController.withPosition(getTargetAngle().getRotations()));
        }
    }
}