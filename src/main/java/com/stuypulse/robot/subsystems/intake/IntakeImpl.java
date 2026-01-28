package com.stuypulse.robot.subsystems.intake;

<<<<<<< Updated upstream
public class IntakeImpl {
=======
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;

// .setControl (new Follower()); //because we have two motors on the intake
// one motor needs to be inverted
public class IntakeImpl extends Intake {
    private final TalonFX intakeMotor1;
    private final TalonFX intakeMotor2;

    public IntakeImpl() {
        intakeMotor1 = new TalonFX(Ports.Intake.LEADER_INTAKE_NEO);
        intakeMotor2 = new TalonFX(Ports.Intake.FOLLOWER_INTAKE_NEO); //TODO: fill in actual ports

        Motors.Intake.intakeMotor1.configure(intakeMotor1);
        Motors.Intake.intakeMotor2.configure(intakeMotor2);
    }

    @Override
    public void periodic() {
        intakeMotor2.setControl(new Follower(Ports.Intake.LEADER_INTAKE_KRAKEN, MotorAlignmentValue.Opposed)); //TODO: fill in later
    
        intakeMotor1.setControl(new VelocityVoltage(getState().getVoltage()));


    }
>>>>>>> Stashed changes
    
}
