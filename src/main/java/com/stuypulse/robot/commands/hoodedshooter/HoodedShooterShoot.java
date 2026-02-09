package com.stuypulse.robot.commands.hoodedshooter;

import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter.HoodedShooterState;

public class HoodedShooterShoot extends HoodedShooterSetState {
    public HoodedShooterShoot() {
        super(HoodedShooterState.SHOOT);
    }
}
