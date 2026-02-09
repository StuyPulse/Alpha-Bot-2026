package com.stuypulse.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.EnabledSubsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
        super.periodic();
        
        if (EnabledSubsystems.INTAKE.get()) {
            intakeLeaderMotor.set(getState().getDutyCycle());
        } else {
            intakeLeaderMotor.stopMotor();
        }

        if (Settings.DEBUG_MODE) {
            SmartDashboard.putNumber("Intake/Leader Target Duty Cycle", getState().getDutyCycle());
            SmartDashboard.putNumber("Intake/Leader Current Duty Cycle", intakeLeaderMotor.get());

            SmartDashboard.putNumber("Intake/Leader Current (amps)", intakeLeaderMotor.getOutputCurrent());
            SmartDashboard.putNumber("Intake/Leader Voltage", intakeLeaderMotor.getAppliedOutput() * intakeLeaderMotor.getBusVoltage());


            SmartDashboard.putNumber("Intake/Follower Current Duty Cycle", intakeFollowerMotor.get());

            SmartDashboard.putNumber("Intake/Follower Current (amps)", intakeFollowerMotor.getOutputCurrent());
            SmartDashboard.putNumber("Intake/Follower Voltage", intakeFollowerMotor.getAppliedOutput() * intakeFollowerMotor.getBusVoltage());
        }
    }
}