package com.stuypulse.robot.subsystems.hoodedshooter;

import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.Robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class HoodedShooter extends SubsystemBase{
    private static final HoodedShooter instance;
    
    private HoodedShooterState state;

    private Rotation2d targetAngle;

    static {
        if (Robot.isReal())
            instance = new HoodedShooterImpl();
        else
            instance = new HoodedShooterSim();
    }
    
    public static HoodedShooter getInstance(){
        return instance;
    }
    
    public enum HoodedShooterState {
        STOW,
        FERRY,
        SHOOT;
    }

    public HoodedShooter() {
        state = HoodedShooterState.STOW;
        targetAngle = Constants.HoodedShooter.MIN_ANGLE;
    }

    public HoodedShooterState getState(){
        return state;
    }

    public void setState(HoodedShooterState state){
        this.state = state;
    }

    public double getTargetRPM() {
        return switch(state) {
            case STOW -> 0;
            case FERRY -> getFerryRPM();
            case SHOOT -> getShootRPM();
        };
    }

    public double getShootRPM() {
        return Constants.HoodedShooter.SHOT_RPM.get();
    }

    public double getFerryRPM() {
        return Constants.HoodedShooter.FERRY_RPM; 
    }

    public boolean shooterAtTolerance() {
        double diff = Math.abs(getTargetRPM() - getShooterRPM());
        return diff > Settings.HoodedShooter.RPM_TOLERANCE;
    }

    public void setTargetAngle(Rotation2d target) {
        targetAngle = target;
    }

    public Rotation2d getTargetAngle() {
        return targetAngle;
    };

    public abstract Rotation2d getHoodAngle();
    public abstract double getShooterRPM();

    @Override
    public void periodic() {
        SmartDashboard.putString("HoodedShooter/State", state.name());
        SmartDashboard.putString("States/HoodedShooter", state.name());

        SmartDashboard.putNumber("HoodedShooter/Target RPM", getTargetRPM());
        SmartDashboard.putNumber("HoodedShooter/Target Angle", getTargetAngle().getDegrees());

        SmartDashboard.putNumber("HoodedShooter/Current RPM", getShooterRPM());
        SmartDashboard.putNumber("HoodedShooter/Current Angle", getHoodAngle().getDegrees());
    }
}