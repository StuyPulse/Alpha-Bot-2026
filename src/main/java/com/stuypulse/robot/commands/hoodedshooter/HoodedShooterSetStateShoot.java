package com.stuypulse.robot.commands.hoodedshooter;

import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter.HoodedShooterState;

public class HoodedShooterSetStateShoot extends HoodedShooterSetState {
    public HoodedShooterSetStateShoot() {
        super(HoodedShooterState.SHOOT);
    }
}
