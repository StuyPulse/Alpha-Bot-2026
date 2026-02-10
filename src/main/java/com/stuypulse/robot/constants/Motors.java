/************************ PROJECT PHIL ************************/
/* Copyright (c) 2024 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot.constants;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.Slot2Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkFlexConfig;

/*-
 * File containing all of the configurations that different motors require.
 *
 * Such configurations include:
 *  - If it is Inverted
 *  - The Idle Mode of the Motor
 *  - The Current Limit
 *  - The Open Loop Ramp Rate
 */
public interface Motors {
    public interface HoodedShooter {
        // TODO: MAKE SURE OF INVERTED VALUES
        public interface Shooter {
            TalonFXConfig SHOOTER_CONFIG = new TalonFXConfig()
                    .withCurrentLimitAmps(80)
                    .withRampRate(0.25)
                    .withNeutralMode(NeutralModeValue.Coast)
                    .withInvertedValue(InvertedValue.CounterClockwise_Positive)
                    .withPIDConstants(Gains.HoodedShooter.Shooter.kP, Gains.HoodedShooter.Shooter.kI,
                            Gains.HoodedShooter.Shooter.kD, 0)
                    .withFFConstants(Gains.HoodedShooter.Shooter.kS, Gains.HoodedShooter.Shooter.kV,
                            Gains.HoodedShooter.Shooter.kA, 0)
                    .withSensorToMechanismRatio(Constants.HoodedShooter.Shooter.GEAR_RATIO);
        }

        public interface Hood {
            TalonFXConfig HOOD_CONFIG = new TalonFXConfig()
                    .withCurrentLimitAmps(80)
                    .withRampRate(0.25)
                    .withNeutralMode(NeutralModeValue.Brake)
                    .withInvertedValue(InvertedValue.CounterClockwise_Positive)
                    .withPIDConstants(Gains.HoodedShooter.Hood.kP, Gains.HoodedShooter.Hood.kI,
                            Gains.HoodedShooter.Hood.kD, 0)
                    .withFFConstants(Gains.HoodedShooter.Hood.kV, Gains.HoodedShooter.Hood.kV,
                            Gains.HoodedShooter.Hood.kA, 0)
                    .withRemoteSensor(Ports.HoodedShooter.Hood.THROUGHBORE_ENCODER, FeedbackSensorSourceValue.RemoteCANcoder, 1);

            SoftwareLimitSwitchConfigs hoodSoftwareLimitSwitchConfigs = new SoftwareLimitSwitchConfigs()
                .withForwardSoftLimitEnable(true)
                .withReverseSoftLimitEnable(true)
                .withForwardSoftLimitThreshold(Constants.HoodedShooter.Hood.MAX_ANGLE.getRotations())
                .withReverseSoftLimitThreshold(Constants.HoodedShooter.Hood.MIN_ANGLE.getRotations());

            CANcoderConfiguration hoodEncoder = new CANcoderConfiguration()
                .withMagnetSensor(new MagnetSensorConfigs()
                        .withSensorDirection(SensorDirectionValue.Clockwise_Positive)
                        .withAbsoluteSensorDiscontinuityPoint(1)
                        .withMagnetOffset(Constants.HoodedShooter.Hood.ENCODER_OFFSET.getRotations()));
        }
    }

    public interface Intake {
        SparkBaseConfig MOTOR_LEADER_CONFIG = new SparkFlexConfig() // TODO: apply later
                .closedLoopRampRate(0.25)
                .inverted(false) // TODO: GET INVERTED VAL
                .idleMode(SparkBaseConfig.IdleMode.kBrake);

        SparkBaseConfig MOTOR_FOLLOW_CONFIG = new SparkFlexConfig()
                .closedLoopRampRate(0.25)
                .inverted(false)
                .idleMode(SparkBaseConfig.IdleMode.kBrake)
                .follow(Ports.Intake.MOTOR_LEAD);
    }

    public interface Spindexer {
        TalonFXConfig spindexerMotors = new TalonFXConfig()
                .withCurrentLimitAmps(80)
                .withRampRate(0.25)
                .withNeutralMode(NeutralModeValue.Brake)
                .withInvertedValue(InvertedValue.Clockwise_Positive) // TODO: Find correct direction for Spindexer
                                                                     // motors
                .withFFConstants(Gains.Spindexer.kS, Gains.Spindexer.kV, Gains.Spindexer.kA, 0)
                .withPIDConstants(Gains.Spindexer.kP, Gains.Spindexer.kI, Gains.Spindexer.kD, 0)
                .withSensorToMechanismRatio(Constants.Spindexer.GEAR_RATIO);
    }

    public interface Turret {
        TalonFXConfig turretMotor = new TalonFXConfig()
                .withCurrentLimitAmps(80)
                .withRampRate(.25)
                .withNeutralMode(NeutralModeValue.Brake)
                .withInvertedValue(InvertedValue.Clockwise_Positive)
                .withSensorToMechanismRatio(Constants.Turret.GEAR_RATIO_MOTOR_TO_MECH);

        SoftwareLimitSwitchConfigs turretSoftwareLimitSwitchConfigs = new SoftwareLimitSwitchConfigs()
                .withForwardSoftLimitEnable(true)
                .withReverseSoftLimitEnable(true)
                .withForwardSoftLimitThreshold(1.5)
                .withReverseSoftLimitThreshold(1.5);

        CANcoderConfiguration turretEncoder17t = new CANcoderConfiguration()
                .withMagnetSensor(new MagnetSensorConfigs()
                        .withSensorDirection(SensorDirectionValue.Clockwise_Positive) // TODO: GET SENSOR DIR
                        .withAbsoluteSensorDiscontinuityPoint(1)
                        .withMagnetOffset(Constants.Turret.Encoder17t.OFFSET.getRotations()));

        CANcoderConfiguration turretEncoder18t = new CANcoderConfiguration()
                .withMagnetSensor(new MagnetSensorConfigs()
                        .withSensorDirection(SensorDirectionValue.Clockwise_Positive) // TODO: GET SENSOR DIR
                        .withAbsoluteSensorDiscontinuityPoint(1)
                        .withMagnetOffset(Constants.Turret.Encoder18t.OFFSET.getRotations()));
    }

    public interface Feeder {
        TalonFXConfig FEEDER_MOTOR_CONFIG = new TalonFXConfig()
                .withCurrentLimitAmps(80)
                .withRampRate(0.25)
                .withNeutralMode(NeutralModeValue.Coast)
                .withInvertedValue(InvertedValue.CounterClockwise_Positive)
                .withFFConstants(Gains.Feeder.kS, Gains.Feeder.kV, Gains.Feeder.kA, 0)
                .withPIDConstants(Gains.Feeder.kP, Gains.Feeder.kI, Gains.Feeder.kD, 0)
                .withSensorToMechanismRatio(Constants.Feeder.GEAR_RATIO);
    }

    public static class TalonFXConfig {
        private final TalonFXConfiguration configuration = new TalonFXConfiguration();
        private final Slot0Configs slot0Configs = new Slot0Configs();
        private final Slot1Configs slot1Configs = new Slot1Configs();
        private final Slot2Configs slot2Configs = new Slot2Configs();
        private final MotorOutputConfigs motorOutputConfigs = new MotorOutputConfigs();
        private final ClosedLoopRampsConfigs closedLoopRampsConfigs = new ClosedLoopRampsConfigs();
        private final OpenLoopRampsConfigs openLoopRampsConfigs = new OpenLoopRampsConfigs();
        private final CurrentLimitsConfigs currentLimitsConfigs = new CurrentLimitsConfigs();
        private final FeedbackConfigs feedbackConfigs = new FeedbackConfigs();
        private final MotionMagicConfigs motionMagicConfigs = new MotionMagicConfigs();

        public void configure(TalonFX motor) {
            motor.getConfigurator().apply(configuration);
        }

        // SLOT 0 CONFIGS

        public TalonFXConfig withPIDConstants(double kP, double kI, double kD, int slot) {
            switch (slot) {
                case 0:
                    slot0Configs.kP = kP;
                    slot0Configs.kI = kI;
                    slot0Configs.kD = kD;
                    configuration.withSlot0(slot0Configs);
                    break;
                case 1:
                    slot1Configs.kP = kP;
                    slot1Configs.kI = kI;
                    slot1Configs.kD = kD;
                    configuration.withSlot1(slot1Configs);
                    break;
                case 2:
                    slot2Configs.kP = kP;
                    slot2Configs.kI = kI;
                    slot2Configs.kD = kD;
                    configuration.withSlot2(slot2Configs);
                    break;
            }
            return this;
        }

        public TalonFXConfig withFFConstants(double kS, double kV, double kA, int slot) {
            return withFFConstants(kS, kV, kA, 0, slot);
        }

        public TalonFXConfig withFFConstants(double kS, double kV, double kA, double kG, int slot) {
            switch (slot) {
                case 0:
                    slot0Configs.kS = kS;
                    slot0Configs.kV = kV;
                    slot0Configs.kA = kA;
                    slot0Configs.kG = kG;
                    configuration.withSlot0(slot0Configs);
                    break;
                case 1:
                    slot1Configs.kS = kS;
                    slot1Configs.kV = kV;
                    slot1Configs.kA = kA;
                    slot1Configs.kG = kG;
                    configuration.withSlot1(slot1Configs);
                    break;
                case 2:
                    slot2Configs.kS = kS;
                    slot2Configs.kV = kV;
                    slot2Configs.kA = kA;
                    slot2Configs.kG = kG;
                    configuration.withSlot2(slot2Configs);
                    break;
            }
            return this;
        }

        public TalonFXConfig withGravityType(GravityTypeValue gravityType) {
            slot0Configs.GravityType = gravityType;
            slot1Configs.GravityType = gravityType;
            slot2Configs.GravityType = gravityType;

            configuration.withSlot0(slot0Configs);
            configuration.withSlot1(slot1Configs);
            configuration.withSlot2(slot2Configs);

            return this;
        }

        // MOTOR OUTPUT CONFIGS

        public TalonFXConfig withInvertedValue(InvertedValue invertedValue) {
            motorOutputConfigs.Inverted = invertedValue;

            configuration.withMotorOutput(motorOutputConfigs);

            return this;
        }

        public TalonFXConfig withNeutralMode(NeutralModeValue neutralMode) {
            motorOutputConfigs.NeutralMode = neutralMode;

            configuration.withMotorOutput(motorOutputConfigs);

            return this;
        }

        // RAMP RATE CONFIGS

        public TalonFXConfig withRampRate(double rampRate) {
            closedLoopRampsConfigs.DutyCycleClosedLoopRampPeriod = rampRate;
            closedLoopRampsConfigs.TorqueClosedLoopRampPeriod = rampRate;
            closedLoopRampsConfigs.VoltageClosedLoopRampPeriod = rampRate;

            openLoopRampsConfigs.DutyCycleOpenLoopRampPeriod = rampRate;
            openLoopRampsConfigs.TorqueOpenLoopRampPeriod = rampRate;
            openLoopRampsConfigs.VoltageOpenLoopRampPeriod = rampRate;

            configuration.withClosedLoopRamps(closedLoopRampsConfigs);
            configuration.withOpenLoopRamps(openLoopRampsConfigs);

            return this;
        }

        // CURRENT LIMIT CONFIGS

        public TalonFXConfig withCurrentLimitAmps(double currentLimitAmps) {
            currentLimitsConfigs.StatorCurrentLimit = currentLimitAmps;
            currentLimitsConfigs.StatorCurrentLimitEnable = true;

            configuration.withCurrentLimits(currentLimitsConfigs);

            return this;
        }

        public TalonFXConfig withSupplyCurrentLimitAmps(double currentLimitAmps) {
            currentLimitsConfigs.SupplyCurrentLimit = currentLimitAmps;
            currentLimitsConfigs.SupplyCurrentLimitEnable = true;

            configuration.withCurrentLimits(currentLimitsConfigs);

            return this;
        }

        // MOTION MAGIC CONFIGS

        public TalonFXConfig withMotionProfile(double maxVelocity, double maxAcceleration) {
            motionMagicConfigs.MotionMagicCruiseVelocity = maxVelocity;
            motionMagicConfigs.MotionMagicAcceleration = maxAcceleration;

            configuration.withMotionMagic(motionMagicConfigs);

            return this;
        }

        // FEEDBACK CONFIGS

        public TalonFXConfig withRemoteSensor(
                int ID, FeedbackSensorSourceValue source, double rotorToSensorRatio) {
            feedbackConfigs.FeedbackRemoteSensorID = ID;
            feedbackConfigs.FeedbackSensorSource = source;
            feedbackConfigs.RotorToSensorRatio = rotorToSensorRatio;

            configuration.withFeedback(feedbackConfigs);

            return this;
        }

        public TalonFXConfig withSensorToMechanismRatio(double sensorToMechanismRatio) {
            feedbackConfigs.SensorToMechanismRatio = sensorToMechanismRatio;

            configuration.withFeedback(feedbackConfigs);

            return this;
        }
    }
}
