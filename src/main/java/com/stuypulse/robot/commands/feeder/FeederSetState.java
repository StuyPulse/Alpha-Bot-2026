package com.stuypulse.robot.commands.feeder;

import com.stuypulse.robot.subsystems.feeder.Feeder;
import com.stuypulse.robot.subsystems.feeder.Feeder.FeederState;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class FeederSetState extends InstantCommand {
    private Feeder feeder;
    private FeederState feederState;
    
    public FeederSetState(FeederState feederState) {
        feeder = Feeder.getInstance();
        feeder.setFeederState(feederState);

        addRequirements(feeder);
    }

    @Override
    public void initialize() {
        feeder.setFeederState(feederState);
    }
}