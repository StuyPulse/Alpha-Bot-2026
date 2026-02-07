package com.stuypulse.robot.subsystems.spindexer;

import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SpindexerImpl extends Spindexer {
    
    private final TalonFX[] spindexerMotors;

    private final DutyCycleOut spindexerDutyCycleController;

    private final Follower spindexerFollow;

    public SpindexerImpl() {
        super();

        spindexerMotors = new TalonFX[] {
            new TalonFX(Ports.Spindexer.LEADER_KRAKEN),
            new TalonFX(Ports.Spindexer.FOLLOWER_KRAKEN)
        };

        Motors.Spindexer.spindexerMotors.configure(spindexerMotors[0]);
        Motors.Spindexer.spindexerMotors.configure(spindexerMotors[1]);

        spindexerDutyCycleController = new DutyCycleOut(0.0);
        spindexerFollow = new Follower(Ports.Spindexer.LEADER_KRAKEN, MotorAlignmentValue.Aligned);

    }

    private void setMotorsBasedOnState() {
        spindexerMotors[0].setControl(spindexerDutyCycleController.withOutput(state.getSpindexerSpeed()));
        spindexerMotors[1].setControl(spindexerFollow);
    }

    private double getRPM() {
        return spindexerMotors[0].getVelocity().getValueAsDouble() * 60.0; // RPS -> RPM
    }

    @Override
    public void periodic() {
        if (Settings.EnabledSubsystems.SPINDEXER.getAsBoolean()) {
            setMotorsBasedOnState();
        }
        SmartDashboard.putNumber("Spindexer/Cake RPM", getRPM());
    }

}
