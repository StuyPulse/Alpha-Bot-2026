package com.stuypulse.robot.commands.feeder;

import com.stuypulse.robot.subsystems.feeder.Feeder.FeederState;

public class FeederFeed extends FeederSetState{
    public FeederFeed(){
        super(FeederState.MAX);
    }
}