package com.stuypulse.robot.commands.auton;

import com.pathplanner.lib.path.PathPlannerPath;
import com.stuypulse.robot.commands.hoodedshooter.HoodedShooterShoot;
import com.stuypulse.robot.commands.intake.IntakeIntake;
import com.stuypulse.robot.commands.intake.IntakeStop;
import com.stuypulse.robot.commands.swerve.SwerveClimbAlign;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class TwoCycleBottom extends SequentialCommandGroup {
    
    public TwoCycleBottom(PathPlannerPath... paths) {

        addCommands(

            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[0])
                .alongWith(new IntakeIntake()),
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[1])
                .alongWith(new IntakeStop()),
            new HoodedShooterShoot(),

            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[2])
                .alongWith(new IntakeIntake()),
            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[3])
                .alongWith(new IntakeStop()),
            new HoodedShooterShoot(),

            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[4]),
                new SwerveClimbAlign()

        );

    }

}
