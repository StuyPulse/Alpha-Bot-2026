/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.turret;


import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Field;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.util.SysId;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.FieldObject2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import java.util.Optional;

public class TurretImpl extends Turret {
    private final TalonFX motor;
    private final CANcoder encoder17t;
    private final CANcoder encoder18t;

    private boolean hasUsedAbsoluteEncoder;
    private Optional<Double> voltageOverride;
    private CRTStatus lastStatus;
    private final PositionVoltage controller;
    

    public TurretImpl() {
        motor = new TalonFX(Ports.Turret.MOTOR, Ports.bus);
        encoder17t = new CANcoder(Ports.Turret.ENCODER17T, Ports.bus);
        encoder18t = new CANcoder(Ports.Turret.ENCODER18T, Ports.bus);

        Motors.Turret.turretMotor.configure(motor);
        encoder17t.getConfigurator().apply(Motors.Turret.turretEncoder17t);
        encoder18t.getConfigurator().apply(Motors.Turret.turretEncoder18t);

        // motor.getConfigurator().apply(Motors.Turret.turretSoftwareLimitSwitchConfigs);

        seedTurret();

        hasUsedAbsoluteEncoder = false;
        voltageOverride = Optional.empty();
        controller = new PositionVoltage(getTargetAngle().getRotations())
            .withEnableFOC(true);
    }

    public enum CRTStatus {
        OK,
        AMBIGUOUS,
        NO_SOLUTION
    }

    private Rotation2d getEncoderPos17t() {
        return Rotation2d.fromRotations(this.encoder17t.getAbsolutePosition().getValueAsDouble());
    }

    private Rotation2d getEncoderPos18t() {
        return Rotation2d.fromRotations(this.encoder18t.getAbsolutePosition().getValueAsDouble());
    }

    public Rotation2d getAbsoluteTurretAngle() {
        final int inverseMod17t = 1;
        final int inverseMod18t = -1;

        final Rotation2d encoder17tPosition = getEncoderPos17t();
        final double numberOfGearTeethRotated17 = (encoder17tPosition.getRotations()
                * (double) Constants.Turret.Encoder17t.TEETH);

        final Rotation2d encoder18tPosition = getEncoderPos18t();
        final double numberOfGearTeethRotated18 = (encoder18tPosition.getRotations()
                * (double) Constants.Turret.Encoder18t.TEETH);

        final double crt_Partial17 = numberOfGearTeethRotated17 * inverseMod17t * Constants.Turret.Encoder17t.TEETH;
        final double crt_Partial18 = numberOfGearTeethRotated18 * inverseMod18t * Constants.Turret.Encoder18t.TEETH;

        double crt_pos = (crt_Partial17 + crt_Partial18)
                % (Constants.Turret.Encoder17t.TEETH * Constants.Turret.Encoder18t.TEETH);

        // Java's % operator is not actually the same as the modulo operator, the lines below account for that 
        crt_pos = (crt_pos < 0) ? (crt_pos + Constants.Turret.Encoder17t.TEETH * Constants.Turret.Encoder18t.TEETH)
                : crt_pos;

        final double turretAngle = (crt_pos / (double) Constants.Turret.BigGear.TEETH);

        return Rotation2d.fromRotations(turretAngle);
    }
    
    public Rotation2d getEasyCRT() {
        double lastIterations;
        double lastErrorRot;

        double matchTolerance = 0.005;

        double ratio1 = Constants.Turret.BigGear.TEETH / Constants.Turret.Encoder17t.TEETH;
        double ratio2 = Constants.Turret.BigGear.TEETH / Constants.Turret.Encoder18t.TEETH;
        double minMechanismRotations = -Constants.Turret.SoftwareLimit.BACKWARDS_MAX_ROTATIONS; 
        double maxMechanismRotations = Constants.Turret.SoftwareLimit.FORWARD_MAX_ROTATIONS; 

        lastIterations = 0;
        lastErrorRot = Double.NaN;

        if (!Double.isFinite(getEncoderPos17t().getRotations())
                || !Double.isFinite(getEncoderPos18t().getRotations())
                || !Double.isFinite(ratio1)
                || !Double.isFinite(ratio2)
                || Math.abs(ratio1) < 1e-12
                || !Double.isFinite(minMechanismRotations)
                || !Double.isFinite(maxMechanismRotations)
                || minMechanismRotations > maxMechanismRotations
                || !Double.isFinite(matchTolerance)
                || matchTolerance < 0.0) {
            return null;
        }

        double bestErr = Double.MAX_VALUE;
        double secondErr = Double.MAX_VALUE;
        double bestRot = Double.NaN;

        // Derive integer wrap-count bounds from the allowed mechanism angle range.
        //
        // abs1 = (ratio1 * mechRot) mod 1
        // ratio1 * mechRot = abs1 + n, where n is an integer.
        // mechRot = (abs1 + n) / ratio1
        //
        // For mechRot within [min, max], n lies within [ratio1*min - abs1, ratio1*max - abs1]
        // (endpoints swap if ratio1 is negative).
        double abs1 = getEncoderPos17t().getRotations();
        double nMinD = Math.min(ratio1 * minMechanismRotations, ratio1 * maxMechanismRotations) - abs1;
        double nMaxD = Math.max(ratio1 * minMechanismRotations, ratio1 * maxMechanismRotations) - abs1;
        int minN = (int) Math.floor(nMinD) - 1;
        int maxN = (int) Math.ceil(nMaxD) + 1;

        for (int n = minN; n <= maxN; n++) {
            lastIterations++;

            double mechRot = (abs1 + n) / ratio1;
            if (mechRot < minMechanismRotations - 1e-6 || mechRot > maxMechanismRotations + 1e-6) {
                continue;
            }

            double predicted2 = MathUtil.inputModulus(ratio2 * mechRot, 0.0, 1.0);
            double err = modularError(predicted2, getEncoderPos18t().getRotations());

            if (err < bestErr) {
                secondErr = bestErr;
                bestErr = err;
                bestRot = mechRot;
            } else if (err < secondErr) {
                secondErr = err;
            }
        }

        if (!Double.isFinite(bestRot) || bestErr > matchTolerance) {
            lastStatus = CRTStatus.NO_SOLUTION;
            lastErrorRot = bestErr;
            return null;
        }

        // If there are two nearly-equal matches within tolerance, the solution is ambiguous.
        if (secondErr <= matchTolerance && Math.abs(secondErr - bestErr) < 1e-3) {
            lastStatus = CRTStatus.AMBIGUOUS;
            lastErrorRot = bestErr;
            return null;
        }

        lastStatus = CRTStatus.OK;
        lastErrorRot = bestErr;
        return Rotation2d.fromRotations(bestErr);
    }    

    private static double modularError(double a, double b) {
        double diff = Math.abs(a - b);
        return diff > 0.5 ? 1.0 - diff : diff;
    }
    @Override
    public Rotation2d getAngle() {
        return Rotation2d.fromDegrees(motor.getPosition().getValueAsDouble());
    }

    @Override
    public boolean atTargetAngle() {
        return Math.abs(getAngle().minus(getTargetAngle()).getDegrees() + 180.0) < Settings.Turret.TOLERANCE_DEG;
    }

    private double getDelta(double target, double current) {
        double delta = (target - current) % 360;

        if (delta > 180.0) delta -= 360;
        else if (delta < -180) delta += 360;

        if (Math.abs(current + delta) < Constants.Turret.RANGE) return delta;
        
        return delta < 0 ? delta + 360 : delta - 360;
    }

    public void seedTurret() {
        motor.setPosition(0.0);
    }
    
    @Override
    public void periodic() {
        super.periodic();

        // if (!hasUsedAbsoluteEncoder || getAbsoluteTurretAngle().getRotations() > 1.0 || getAngle().getRotations() < 0.0) {
        //     motor.setPosition((getAbsoluteTurretAngle().getDegrees() % 360.0) / 360.0);
        //     hasUsedAbsoluteEncoder = true;
        //     System.out.println("Absolute Encoder Reset triggered");
        // }

        double currentAngle = getAngle().getDegrees();
        double actualTargetDeg = currentAngle + getDelta(getTargetAngle().getDegrees(), currentAngle);

        SmartDashboard.putNumber("Turret/Delta (deg)", getDelta(getTargetAngle().getDegrees(), getAngle().getDegrees()));
        SmartDashboard.putNumber("Turret/Actual Target (deg)", actualTargetDeg);

        if (Settings.EnabledSubsystems.TURRET.get() && getState() != TurretState.IDLE) {
            if (voltageOverride.isPresent()) {
                motor.setVoltage(voltageOverride.get());
            } else {
                motor.setControl(controller.withPosition(actualTargetDeg / 360.0));
            }
        } else {
            motor.stopMotor();
        }

        if (Settings.DEBUG_MODE) {
            SmartDashboard.putNumber("Turret/Encoder18t Abs Position (Rot)", encoder18t.getAbsolutePosition().getValueAsDouble());
            SmartDashboard.putNumber("Turret/Encoder17t Abs Position (Rot)", encoder17t.getAbsolutePosition().getValueAsDouble());
            SmartDashboard.putNumber("Turret/CRT Position (Rot)", getAbsoluteTurretAngle().getRotations());
            SmartDashboard.putNumber("Turret/Relative Encoder Position (deg)", motor.getPosition().getValueAsDouble() * 360.0);
            SmartDashboard.putNumber("Turret/Voltage", motor.getMotorVoltage().getValueAsDouble());
            SmartDashboard.putNumber("Turret/Error", motor.getClosedLoopError().getValueAsDouble() * 360.0);

            // Pose2d robotPose = CommandSwerveDrivetrain.getInstance().getPose();
            // Translation2d turretPose = robotPose.getTranslation().plus(Constants.Turret.TURRET_OFFSET.getTranslation().rotateBy(robotPose.getRotation()));

            // SmartDashboard.putNumber("Turret/OG turret from hub", turretPose.getDistance(Field.hubCenter.getTranslation()));
        }
    }

    @Override
    public SysIdRoutine getSysIdRoutine() {
        return SysId.getRoutine(
                2,
                6,
                "Turret",
                voltage -> setVoltageOverride(Optional.of(voltage)),
                () -> this.motor.getPosition().getValueAsDouble(),
                () -> this.motor.getVelocity().getValueAsDouble(),
                () -> this.motor.getMotorVoltage().getValueAsDouble(),
                getInstance());
    }

    private void setVoltageOverride(Optional<Double> volts) {
        this.voltageOverride = volts;
    }
}
