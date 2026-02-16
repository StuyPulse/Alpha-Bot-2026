/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.auton;

import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import com.pathplanner.lib.path.PathPlannerPath;

public class StraightLine extends SequentialCommandGroup {
    
    public StraightLine(PathPlannerPath... paths) {

        addCommands(

            CommandSwerveDrivetrain.getInstance().followPathCommand(paths[0])

        );

    }

}
