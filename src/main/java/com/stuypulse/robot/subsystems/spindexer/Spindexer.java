/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.subsystems.spindexer;

import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public abstract class Spindexer extends SubsystemBase {
    private static final Spindexer instance;

    protected SpindexerState state;

    static {
        instance = new SpindexerImpl();
    }

    public static Spindexer getInstance() {
        return instance;
    }

    public Spindexer() {
        this.state = SpindexerState.STOP;
    }

    public enum SpindexerState {
        RUNNING(Settings.Spindexer.RUNNING_SPEED),
        STOP(Settings.Spindexer.STOP_SPEED),
        REVERSE(Settings.Spindexer.REVERSE_SPEED);

        private double targetRPM;

        private SpindexerState(double targetRPM) {
            this.targetRPM = targetRPM;
        }

        public double getTargetRPM() {
            return this.targetRPM;
        }
    }

    public void setState(SpindexerState state) {
        this.state = state;
    }

    public SpindexerState getState() {
        return state;
    }


    public abstract SysIdRoutine getSysIdRoutine();

    @Override
    public void periodic() {
        SmartDashboard.putString("Spindexer/State", getState().name());
        SmartDashboard.putString("States/Spindexer", getState().name());
    }
}
