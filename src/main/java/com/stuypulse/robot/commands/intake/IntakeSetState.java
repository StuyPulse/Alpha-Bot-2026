package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake;
import com.stuypulse.robot.subsystems.intake.Intake.IntakeState;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeSetState extends InstantCommand{
    Intake intake = Intake.getInstance(); //(3.)
    IntakeState state;

    public IntakeSetState(IntakeState state) {
        this.state = state;

        addRequirements(intake); //...
    }

    @Override 
    public void initialize() {
        //new Intake().setState(state); //are we so serious bro - this was the mistake
        intake.setState(state); //fix, checked in sim everything works
    }
}
