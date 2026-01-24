package com.stuypulse.robot.commands.swerve;

import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.Command;

public class SwerveResetRotation extends Command {
    private final CommandSwerveDrivetrain swerve;

    public SwerveResetRotation() {
        swerve = CommandSwerveDrivetrain.getInstance();
        swerve.resetRotation(swerve.getPose().getRotation());
    }
}
