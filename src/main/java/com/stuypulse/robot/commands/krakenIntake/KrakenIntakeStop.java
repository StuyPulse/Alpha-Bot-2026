package com.stuypulse.robot.commands.krakenIntake;

import com.stuypulse.robot.subsystems.krakenIntake.KrakenIntake.KrakenIntakeRollerState;

public class KrakenIntakeStop extends SetKrakenIntakeState {

    public KrakenIntakeStop() {
        super(KrakenIntakeRollerState.STOP);
    }

}