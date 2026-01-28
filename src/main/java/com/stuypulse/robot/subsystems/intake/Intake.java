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
        INTAKING(3); //TODO: add to settings and reference from settings
        
        private double voltage;
        
        private IntakeState(double voltage){
            this.voltage = voltage;
        }

        public double getVoltage() {
            return voltage;
        }

    }

    private IntakeState state;

    public Intake() {
        state = IntakeState.STOW;
    }

    public IntakeState getState() {
        return state;
    }

    public void setState(IntakeState state) {
        this.state = state;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Subsystems/Intake", "INTAKE");
    }
}
