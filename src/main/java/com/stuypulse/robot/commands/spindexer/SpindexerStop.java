package com.stuypulse.robot.commands.spindexer;

import com.stuypulse.robot.subsystems.spindexer.Spindexer.SpindexerState;

public class SpindexerStop extends SpindexerSetState {
    public SpindexerStop() {
        super(SpindexerState.STOP);
    }
}
