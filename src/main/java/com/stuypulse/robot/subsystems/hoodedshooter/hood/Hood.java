package com.stuypulse.robot.subsystems.hoodedshooter.hood;

import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.util.hoodedshooter.HoodAngleCalculator;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public abstract class Hood extends SubsystemBase{
    private static final Hood instance;
    
    private HoodState state;

    static {
        instance = new HoodImpl();
    }
    
    public static Hood getInstance(){
        return instance;
    }
    
    public enum HoodState {
        STOW,
        FERRY,
        SHOOT,
        HUB,
        LEFT_CORNER,
        RIGHT_CORNER,
        IDLE;
    }

    public Hood() {
        state = HoodState.STOW;
    }

    public HoodState getState(){
        return state;
    }

    public void setState(HoodState state){
        this.state = state;
    }

    public Rotation2d getTargetAngle() {
        return switch(state) {
            case STOW -> Constants.HoodedShooter.Hood.MIN_ANGLE;
            case FERRY -> Rotation2d.fromDegrees(100);
            case SHOOT -> HoodAngleCalculator.calculateHoodAngleSOTM().get();
            case HUB -> Constants.HoodedShooter.Hood.HUB_ANGLE;
            case LEFT_CORNER -> Constants.HoodedShooter.Hood.LEFT_CORNER_ANGLE;
            case RIGHT_CORNER -> Constants.HoodedShooter.Hood.RIGHT_CORNER_ANGLE;
            case IDLE -> getHoodAngle();
        };
    }

    public boolean atTolerance() {
        return Math.abs(getHoodAngle().getDegrees() - getTargetAngle().getDegrees()) < Settings.HoodedShooter.HOOD_TOLERANCE_DEG;
    }

    public abstract Rotation2d getHoodAngle();

    public abstract SysIdRoutine getHoodSysIdRoutine();

    @Override
    public void periodic() {
        SmartDashboard.putString("Hood/State", state.name());
        SmartDashboard.putString("States/Hood", state.name());
    }
}