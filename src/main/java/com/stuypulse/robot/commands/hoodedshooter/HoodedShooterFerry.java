package com.stuypulse.robot.commands.hoodedshooter;

import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter.HoodedShooterState;

public class HoodedShooterFerry extends HoodedShooterSetState {
    public HoodedShooterFerry(){
        super(HoodedShooterState.FERRY);
    }
}