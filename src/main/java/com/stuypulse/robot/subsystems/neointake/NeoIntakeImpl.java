package com.stuypulse.robot.subsystems.neointake;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Motors;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NeoIntakeImpl extends NeoIntake {

    private final SparkFlex[] rollerMotors;
    
    private final RelativeEncoder rollerMotor1Encoder;
    private final RelativeEncoder rollerMotor2Encoder;

    public NeoIntakeImpl() {
        super();

        rollerMotors = new SparkFlex[] {
            new SparkFlex(Ports.Intakes.NeoIntake.ROLLER, MotorType.kBrushless),
            new SparkFlex(Ports.Intakes.NeoIntake.ROLLER2, MotorType.kBrushless)
        };

        Motors.Intakes.NeoIntake.motorConfig.follow(rollerMotors[0]);
        rollerMotors[1].configure(Motors.Intakes.NeoIntake.motorConfig, ResetMode.kResetSafeParameters,
            PersistMode.kPersistParameters);

        Motors.Intakes.NeoIntake.motorConfig.disableFollowerMode();
        rollerMotors[0].configure(Motors.Intakes.NeoIntake.motorConfig, ResetMode.kResetSafeParameters,
            PersistMode.kPersistParameters);

        rollerMotor1Encoder = rollerMotors[0].getEncoder();
        rollerMotor2Encoder = rollerMotors[1].getEncoder();
    }

    private void setMotorBasedOnState() {
        double speed = MathUtil.clamp(state.getIntakeRollerSpeed(), -1.0, 1.0);
        rollerMotors[0].set(speed);
    }

    private double getAvgIntakeRPM() {
        return (rollerMotor1Encoder.getVelocity() + rollerMotor2Encoder.getVelocity()) / 2;
    }

    private double getBusVoltage1() {
        return rollerMotors[0].getBusVoltage();
    }

    private double getBusVoltage2() {
        return rollerMotors[1].getBusVoltage();
    }

    @Override
    public void periodic() {
        setMotorBasedOnState();
        SmartDashboard.putNumber("Intake/Neo Intake/Rollers/Current RPM (Average of two encoders)", getAvgIntakeRPM());
        SmartDashboard.putNumber("Intake/Neo Intake/Rollers/Bus Voltage (Motor 1)", getBusVoltage1());
        SmartDashboard.putNumber("Intake/Neo Intake/Rollers/Bus Voltage (Motor 2)", getBusVoltage2());
        SmartDashboard.putString("Intake/Neo Intake/Rollers/Current State", getState().toString());
    }

}