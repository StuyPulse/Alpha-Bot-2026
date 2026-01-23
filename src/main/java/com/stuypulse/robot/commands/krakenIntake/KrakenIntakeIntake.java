package com.stuypulse.robot.commands.krakenIntake;

import com.stuypulse.robot.subsystems.krakenIntake.KrakenIntake.KrakenIntakeRollerState;

public class KrakenIntakeIntake extends SetKrakenIntakeState {

    public KrakenIntakeIntake() {
        super(KrakenIntakeRollerState.INTAKING);
    }

}