package com.stuypulse.robot.subsystems.turret;

import com.stuypulse.robot.Robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class Turret extends SubsystemBase {
    private static final Turret instance;

    static {
        instance = Robot.isReal() ? new TurretImpl() : new TurretSim();
    }

    public static Turret getInstance() {
        return instance;
    }

    public enum TurretState {
        IDLE,
        SHOOTING,
        FERRYING;
    }

    protected TurretState state;
    
    public Turret() {
        this.state = TurretState.IDLE;
    }
    
    public void setState(TurretState state) {
        this.state = state;
    }

    public TurretState getState() {
        return this.state;
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("Turret/State", state.name());
    }
}