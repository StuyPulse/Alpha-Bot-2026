package com.stuypulse.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

// .setControl (new Follower()); //because we have two motors on the intake
// one motor needs to be inverted
public class IntakeImpl extends Intake {
    private final SparkFlex intakeLeaderMotor;
    private final SparkFlex intakeFollowerMotor;

    public IntakeImpl() {
        intakeLeaderMotor = new SparkFlex(Ports.Intake.LEADER_INTAKE_NEO, MotorType.kBrushless);
        intakeFollowerMotor = new SparkFlex(Ports.Intake.FOLLOWER_INTAKE_NEO, MotorType.kBrushless); //TODO: fill in actual ports

        intakeLeaderMotor.configure(Motors.Intake.intakeLeaderMotor, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        intakeFollowerMotor.configure(Motors.Intake.intakeFollowerMotor, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void periodic() {
        // OLD KRAKEN CODE: intakeFollowerMotor.set(new Follower(Ports.Intake.LEADER_INTAKE_KRAKEN, MotorAlignmentValue.Opposed)); //TODO: fill in later
    
        intakeLeaderMotor.set(1); //if this doesn't work then follower didn't work in configs. Try without Follower

    }
    
}
