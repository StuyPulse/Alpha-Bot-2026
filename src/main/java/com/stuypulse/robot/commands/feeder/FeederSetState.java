/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.commands.feeder;

import com.stuypulse.robot.subsystems.feeder.Feeder;
import com.stuypulse.robot.subsystems.feeder.Feeder.FeederState;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class FeederSetState extends InstantCommand {
    private Feeder feeder;
    private FeederState feederState;
    
    public FeederSetState(FeederState feederState) {
        feeder = Feeder.getInstance();
        this.feederState = feederState;

        addRequirements(feeder);
    }

    @Override
    public void initialize() {
        feeder.setState(feederState);
    }
}