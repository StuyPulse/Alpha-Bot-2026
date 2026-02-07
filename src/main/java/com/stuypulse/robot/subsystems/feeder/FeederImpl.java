package com.stuypulse.robot.subsystems.feeder;

import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FeederImpl extends Feeder {
    private final TalonFX motor;

    protected FeederImpl() {
        super();
        
        motor = new TalonFX(Ports.Feeder.MOTOR);
        Motors.Feeder.FEEDER_MOTOR_CONFIG.configure(motor);
    }

    @Override
    public void periodic() {
        super.periodic();

        if (EnabledSubsystems.FEEDER.get()) {
            motor.set(getFeederState().getTargetDutyCycle());
        } else {
            motor.stopMotor();
        }

        SmartDashboard.putNumber("Feeder/Target Duty Cycle", getFeederState().getTargetDutyCycle());
        SmartDashboard.putNumber("Feeder/Motor Velocity", motor.getVelocity().getValueAsDouble() * 60.0);

        SmartDashboard.putNumber("Feeder/Current (amps)", motor.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Feeder/Voltage", motor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Feeder/Supply Current", motor.getSupplyCurrent().getValueAsDouble());
    }
}