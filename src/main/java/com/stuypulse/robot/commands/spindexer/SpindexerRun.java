package com.stuypulse.robot.commands.spindexer;

import com.stuypulse.robot.subsystems.spindexer.Spindexer.SpindexerState;

public class SpindexerRun extends SpindexerSetState {
    public SpindexerRun() {
        super(SpindexerState.RUNNING);
    }
}
