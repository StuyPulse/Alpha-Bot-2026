package com.stuypulse.robot.commands.hoodedshooter;

import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter.HoodedShooterState;

public class HoodedShooterSetStateStow extends HoodedShooterSetState{
    public HoodedShooterSetStateStow(){
        super(HoodedShooterState.STOW);
    }
}