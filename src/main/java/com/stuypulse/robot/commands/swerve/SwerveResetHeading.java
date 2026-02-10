package com.stuypulse.robot.commands.swerve;

import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SwerveResetHeading extends InstantCommand{
    public SwerveResetHeading() {
        super(() -> CommandSwerveDrivetrain.getInstance().resetRotation(Rotation2d.kZero));
    }
}
