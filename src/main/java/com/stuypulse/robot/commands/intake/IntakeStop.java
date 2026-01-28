package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake.IntakeState;

public class IntakeStop extends IntakeSetState {
    public IntakeStop() {
        super(IntakeState.STOP);
    }
}
