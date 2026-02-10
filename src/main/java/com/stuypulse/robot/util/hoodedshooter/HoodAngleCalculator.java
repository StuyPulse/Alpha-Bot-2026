package com.stuypulse.robot.util.hoodedshooter;

import com.stuypulse.robot.Robot;
import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Field;
import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.util.hoodedshooter.ShotCalculator.AlignAngleSolution;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

import java.util.function.Supplier;

public class HoodAngleCalculator {
    
    private HoodAngleCalculator() {

    }

    public static Supplier<Rotation2d> calculateHoodAngleSOTM() {
        return () -> {
            CommandSwerveDrivetrain swerve = CommandSwerveDrivetrain.getInstance();
            HoodedShooter hdsr = HoodedShooter.getInstance();
            
            Pose2d currentPose = swerve.getPose();
            
            ChassisSpeeds robotRelSpeeds = swerve.getChassisSpeeds();
            ChassisSpeeds fieldRelSpeeds = ChassisSpeeds.fromRobotRelativeSpeeds(
                robotRelSpeeds, 
                currentPose.getRotation()
            );
            
            Pose3d targetPose = Field.hubCenter3d;
            double axMetersPerSecondSquared = swerve.getPigeon2().getAccelerationX().getValueAsDouble();
            double ayMetersPerSecondSquared = swerve.getPigeon2().getAccelerationY().getValueAsDouble();
            
            double shooterRPS = hdsr.getTargetRPM() / 60.0;
            
            AlignAngleSolution sol = ShotCalculator.solveShootOnTheFly(
                new Pose3d(currentPose.plus(Constants.HoodedShooter.TURRET_OFFSET)),
                targetPose,
                axMetersPerSecondSquared,
                ayMetersPerSecondSquared,
                fieldRelSpeeds, // current speeds
                shooterRPS,
                Constants.Align.MAX_ITERATIONS,
                Constants.Align.TIME_TOLERANCE
            );
            
            return sol.launchPitchAngle();
        };
    }
}