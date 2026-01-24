package com.stuypulse.robot.subsystems.krakenintake;

import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Motors;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class KrakenIntakeImpl extends KrakenIntake {

    private final TalonFX rollerMotor;

    public KrakenIntakeImpl(int krakenID) {
        super();

        rollerMotor = new TalonFX(krakenID);
        Motors.Intakes.KrakenIntake.motorConfig.configure(rollerMotor);
    }

    private void setMotorBasedOnState() {
        double speed = MathUtil.clamp(state.getKrakenIntakeRollerSpeed(), -1.0, 1.0);
        rollerMotor.set(speed);
    }

    private double getIntakeRPM() {
        return rollerMotor.getVelocity().getValueAsDouble() * 60.0;
    }

    private double getSupplyVoltage() {
        return rollerMotor.getSupplyVoltage().getValueAsDouble();
    }

    private double getMotorVoltage() {
        return rollerMotor.getMotorVoltage().getValueAsDouble();
    }

    @Override
    public void periodic() {
        setMotorBasedOnState();
        SmartDashboard.putNumber("Intake/Kraken Intake/Rollers/Current RPM", getIntakeRPM());
        SmartDashboard.putNumber("Intake/Kraken Intake/Rollers/Supply Voltage", getSupplyVoltage());
        SmartDashboard.putNumber("Intake/Kraken Intake/Rollers/Motor Voltage", getMotorVoltage());
        SmartDashboard.putString("Intake/Kraken Intake/Rollers/Current State", getState().toString());
    }

}