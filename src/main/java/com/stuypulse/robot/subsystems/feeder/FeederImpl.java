package com.stuypulse.robot.subsystems.feeder;

import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FeederImpl extends Feeder {
    private final TalonFX feederMotor;

    protected FeederImpl() {
        super();
        
        this.feederMotor = new TalonFX(Ports.Feeder.MOTOR);
        Motors.Feeder.FEEDER_MOTOR_CONFIG.configure(feederMotor);
    }

    public double getCurrentFeederRPM() {
        return feederMotor.getVelocity().getValueAsDouble() * 60.0;
    }

    public double getTargetRPM(){
        return getFeederState().getFeederSpeed();
    }

    @Override
    public void periodic() {
        super.periodic();
        feederMotor.set(getFeederState().getFeederSpeed());

        SmartDashboard.putNumber("Feeder/Motor Speed (RPM)", getCurrentFeederRPM());
    }
}