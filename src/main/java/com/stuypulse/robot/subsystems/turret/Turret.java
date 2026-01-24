package com.stuypulse.robot.subsystems.turret;

import com.stuypulse.robot.Robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

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
    
    public Turret() {
        this.state = TurretState.IDLE;
    }

    public Rotation2d getTargetAngle() {
        return switch (getState()) {
            case IDLE -> new Rotation2d(); 
            case FERRYING -> getFerryAngle();
            case SHOOTING -> getPointAtHubAngle();
        };
    }

    protected TurretState state;

    public abstract boolean atTargetAngle();
    public abstract Rotation2d getTurretAngle();
    
    public abstract Rotation2d getFerryAngle();
    public abstract Rotation2d getPointAtHubAngle();

    public abstract SysIdRoutine getSysIdRoutine();
   
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