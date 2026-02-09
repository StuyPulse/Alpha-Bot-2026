package com.stuypulse.robot.subsystems.feeder;

import java.util.Optional;

import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;
import com.stuypulse.robot.util.SysId;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class FeederImpl extends Feeder {
    private final TalonFX motor;
    private Optional<Double> voltageOverride;

    protected FeederImpl() {
        super();
        
        motor = new TalonFX(Ports.Feeder.MOTOR);
        Motors.Feeder.FEEDER_MOTOR_CONFIG.configure(motor);

        voltageOverride = Optional.empty();
    }

    @Override
    public SysIdRoutine getSysIdRoutine() {
        return SysId.getRoutine(
            1, 
            5, 
            "Feeder", 
            voltage -> setVoltageOverride(Optional.of(voltage)), 
            () -> motor.getPosition().getValueAsDouble(), 
            () -> motor.getVelocity().getValueAsDouble(), 
            () -> motor.getMotorVoltage().getValueAsDouble(), 
            getInstance()
        );
    }

    private void setVoltageOverride(Optional<Double> volts) {
        this.voltageOverride = volts;
    }

    @Override
    public void periodic() {
        super.periodic();

        if (EnabledSubsystems.FEEDER.get()) {
            if(!voltageOverride.isEmpty()) {
                motor.setVoltage(voltageOverride.get());
            }
            else motor.setControl(new VelocityVoltage(getFeederState().getTargetRPM()  / 60.0));
        } else {
            motor.stopMotor();
        }

        SmartDashboard.putNumber("Feeder/Target RPM", getFeederState().getTargetRPM());
        SmartDashboard.putNumber("Feeder/Motor RPM", motor.getVelocity().getValueAsDouble() * 60.0);

        SmartDashboard.putNumber("Feeder/Current (amps)", motor.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Feeder/Voltage", motor.getMotorVoltage().getValueAsDouble());
        SmartDashboard.putNumber("Feeder/Supply Current", motor.getSupplyCurrent().getValueAsDouble());
    }
}