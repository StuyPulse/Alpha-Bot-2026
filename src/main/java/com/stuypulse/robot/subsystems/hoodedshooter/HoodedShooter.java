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

    public abstract double getCurrentRPS();
    public abstract Rotation2d getCurrentAngle();

    public HoodedShooterState getState(){
        return state;
    }

    public void setState(HoodedShooterState state){
        this.state = state;
    }

    public void setTargetAngle(Rotation2d angle) {
        targetAngle = angle;
    }
 
    public Rotation2d getTargetAngle() {
        return targetAngle;
    }

    public double getTargetRPM() {
        return switch(state) {
            case STOW -> 0;
            case FERRY -> getFerryRPM();
            case SHOOT -> getShootRPM();
        };
    }


    public double getShootRPM() {
        return Constants.HoodedShooter.SHOT_RPM;
    }

    public double getFerryRPM() {
        return Constants.HoodedShooter.FERRY_RPM; 
    }

    public boolean spunUp() {
        double diff = Math.abs(getTargetRPM() - getCurrentRPS() * 60);
        return (diff > Settings.Shooter.shooterRpmTolerance.getAsDouble());
    }

    @Override 
    public void periodic() {
        SmartDashboard.putString("hdsr/state", getState().name());
        SmartDashboard.putNumber("hdsr/targetAngle", getTargetAngle().getDegrees());
        SmartDashboard.putNumber("hdsr/currentAngle", getCurrentAngle().getDegrees());
        SmartDashboard.putNumber("hdsr/currentRPM", getCurrentRPS() * 60);
        SmartDashboard.putNumber("hdsr/targetRPM", getTargetRPM());
    }
}
