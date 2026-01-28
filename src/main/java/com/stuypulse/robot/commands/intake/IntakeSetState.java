package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake;
import com.stuypulse.robot.subsystems.intake.Intake.IntakeState;
import com.stuypulse.robot.subsystems.intake.IntakeImpl;
        
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeSetState extends InstantCommand{
    IntakeImpl intake = Intake.getInstance();
    IntakeState state;

    public IntakeSetState(IntakeState state) {
        this.state = state;
    }

    @Override 
    public void initialize() {
        new Intake().setState(state);
    }
}
