package com.stuypulse.robot.commands.hoodedshooter;

import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter.HoodedShooterState;

public class HoodedShooterSetStateFerry extends HoodedShooterSetState {
    public HoodedShooterSetStateFerry(){
        super(HoodedShooterState.FERRY);
    }
}