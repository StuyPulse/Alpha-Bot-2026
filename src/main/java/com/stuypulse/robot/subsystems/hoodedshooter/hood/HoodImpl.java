package com.stuypulse.robot.subsystems.hoodedshooter.hood;

import java.util.Optional;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;
import com.stuypulse.robot.util.SysId;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class HoodImpl extends Hood {
    private final TalonFX hoodMotor;
    private final CANcoder hoodEncoder;

    // Controllers
    private final PositionVoltage hoodController;

    private Optional<Double> hoodVoltageOverride;
    
    private boolean hasSeededHood;

    public HoodImpl() {

        hoodController = new PositionVoltage(getTargetAngle().getRotations());

        hoodMotor = new TalonFX(Ports.HoodedShooter.Hood.MOTOR);
        hoodEncoder = new CANcoder(Ports.HoodedShooter.Hood.THROUGHBORE_ENCODER);

        Motors.HoodedShooter.Hood.HOOD_CONFIG.configure(hoodMotor);

        hoodMotor.getConfigurator().apply(Motors.HoodedShooter.Hood.hoodSoftwareLimitSwitchConfigs);

        hoodVoltageOverride = Optional.empty();
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

    public void setVoltageOverride(Optional<Double> hoodVoltageOverride) {
        this.hoodVoltageOverride = hoodVoltageOverride;
    }

    @Override 
    public void periodic() {
        super.periodic();

        SmartDashboard.putNumber("HoodShooter/Hood absolute angle (deg)", getHoodAngle().getDegrees());

        if (!hasSeededHood) {
            hoodMotor.setPosition(hoodEncoder.getPosition().getValueAsDouble());
            hasSeededHood = true;
        }

        if (!EnabledSubsystems.HOOD.get() || getState() == HoodState.STOW) {
            hoodMotor.stopMotor();
        } else {
            hoodMotor.setControl(hoodController.withPosition(getTargetAngle().getRotations()));
        }
    }
}