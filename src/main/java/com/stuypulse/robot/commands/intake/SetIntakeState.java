package com.stuypulse.robot.commands.intake;

import com.stuypulse.robot.subsystems.intake.Intake;
import com.stuypulse.robot.subsystems.intake.Intake.IntakeState;
import com.stuypulse.robot.subsystems.intake.IntakeImpl;
        
import edu.wpi.first.wpilibj2.command.InstantCommand;
//WHAT WE NEED:
//Constructor
//super() -> good practice
//execute OR initialize
//INTIALIZE

//WHAT DOES OUR CONSTRUCTOR
public class SetIntakeState extends InstantCommand{
    IntakeImpl intake = Intake.getInstance();
    IntakeState state = IntakeState.STOW;

    public SetIntakeState(IntakeState state) {
        this.state = state;
    }
    
    @Override 
    public void initialize() {
        new Intake().setState(state);
    }    
    
    //
    
    //SetStowIntake
}
