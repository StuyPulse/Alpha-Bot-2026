package com.stuypulse.robot.commands.krakenIntake;

import com.stuypulse.robot.subsystems.krakenIntake.KrakenIntake.KrakenIntakeRollerState;

public class KrakenIntakeOuttake extends SetKrakenIntakeState {

    public KrakenIntakeOuttake() {
        super(KrakenIntakeRollerState.OUTTAKING);
    }

}