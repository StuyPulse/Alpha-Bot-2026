package com.stuypulse.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

public class IntakeImpl extends Intake {
    private final SparkFlex intakeLeaderMotor;
    private final SparkFlex intakeFollowerMotor;

    public IntakeImpl() {
        intakeLeaderMotor = new SparkFlex(Ports.Intake.MOTOR_LEAD, MotorType.kBrushless);
        intakeFollowerMotor = new SparkFlex(Ports.Intake.MOTOR_FOLLOW, MotorType.kBrushless);

        intakeLeaderMotor.configure(Motors.Intake.MOTOR_LEADER_CONFIG, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        intakeFollowerMotor.configure(Motors.Intake.MOTOR_FOLLOW_CONFIG, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void periodic() {
        intakeLeaderMotor.set(getState().getSpeed());
    }
}