package com.stuypulse.robot.subsystems.intake;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{
    private static IntakeImpl instance;

    static {
        instance = new IntakeImpl();
    }

    public static IntakeImpl getInstance() {
        return instance;
    }

    public enum IntakeState {
        STOW(0),
        INTAKING(1),
        OUTTAKING(-1); //don't think that we need this but we have it.
        //TODO: add to settings and reference from settings
        
        private double speed;
        
        private IntakeState(double speed){
            this.speed = speed;
        }

        public double getSpeed() {
            return speed;
        }

    }

    private IntakeState state;

    public Intake() {
        state = IntakeState.STOW;
    }

    public IntakeState getState() {
        return state;
    }


    private double speed;


    public void setState(IntakeState state) {
        this.state = state;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Subsystems/Intake", "INTAKE");
    }

}

