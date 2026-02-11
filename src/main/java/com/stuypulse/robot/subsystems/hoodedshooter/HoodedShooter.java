package com.stuypulse.robot.subsystems.hoodedshooter;

import com.stuypulse.robot.subsystems.hoodedshooter.hood.Hood;
import com.stuypulse.robot.subsystems.hoodedshooter.hood.Hood.HoodState;
import com.stuypulse.robot.subsystems.hoodedshooter.shooter.Shooter;
import com.stuypulse.robot.subsystems.hoodedshooter.shooter.Shooter.ShooterState;
import com.stuypulse.robot.Robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HoodedShooter extends SubsystemBase {

    private static final HoodedShooter instance;

    static {
        if (Robot.isReal())
            instance = new HoodedShooter();
        else
            instance = new HoodedShooterSim();
    }
    
    public static HoodedShooter getInstance(){
        return instance;
    }

    private HoodedShooterState state;

    private final Hood hood;
    private final Shooter shooter;

    public HoodedShooter() {
        state = HoodedShooterState.STOW;
        hood = Hood.getInstance();
        shooter = Shooter.getInstance();
    }
    
    public enum HoodedShooterState {
        STOW(HoodState.STOW, ShooterState.STOP),
        SHOOT(HoodState.SHOOT, ShooterState.SHOOT),
        FERRY(HoodState.FERRY, ShooterState.FERRY);

        private HoodState hoodState;
        private ShooterState shooterState;

        private HoodedShooterState(HoodState hoodState, ShooterState shooterState) {
            this.hoodState = hoodState;
            this.shooterState = shooterState;
        }

        public HoodState getHoodState() {
            return hoodState;
        }

        public ShooterState getShooterState() {
            return shooterState;
        }
    }

    public void setState(HoodedShooterState state){
        this.state = state;
        hood.setState(state.getHoodState());
        shooter.setState(state.getShooterState());
    }

    public HoodedShooterState getState(){
        return state;
    }

    public boolean isShooterAtTolerance() {
        return shooter.atTolerance();
    }

    public boolean isHoodAtTolerance() {
        return hood.atTolerance();
    }

    public double getTargetRPM() {
        return shooter.getTargetRPM();
    }

    public Rotation2d getTargetAngle() {
        return hood.getTargetAngle();
    }

    public double getShooterRPM() {
        return shooter.getShooterRPM();
    }

    public Rotation2d getHoodAngle() {
        return hood.getHoodAngle();
    }

    @Override
    public void periodic() {
        SmartDashboard.putString("HoodedShooter/State", state.name());
        SmartDashboard.putString("States/HoodedShooter", state.name());

        SmartDashboard.putNumber("HoodedShooter/Target RPM", shooter.getTargetRPM());
        SmartDashboard.putNumber("HoodedShooter/Target Angle", hood.getTargetAngle().getDegrees());

        SmartDashboard.putNumber("HoodedShooter/Current RPM", getShooterRPM());
        SmartDashboard.putNumber("HoodedShooter/Current Angle", getHoodAngle().getDegrees());
    }
}