package com.stuypulse.robot.commands.hoodedshooter;

import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter.HoodedShooterState;

public class HoodedShooterReverse extends HoodedShooterSetState {
    public HoodedShooterReverse() {
        super(HoodedShooterState.REVERSE);
    }
}
