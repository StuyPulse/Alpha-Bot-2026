package com.stuypulse.robot.commands.hoodedshooter;

import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter.HoodedShooterState;

public class HoodedShooterStow extends HoodedShooterSetState{
    public HoodedShooterStow(){
        super(HoodedShooterState.STOW);
    }
}