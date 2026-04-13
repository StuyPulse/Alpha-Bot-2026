package com.stuypulse.robot.commands.vision;

import com.stuypulse.robot.constants.Settings;

public class SetLimelightIMUAssist extends SetIMUMode {
    public SetLimelightIMUAssist() {
        super(Settings.Vision.IMU_ASSIST_MODE);
    }
}
