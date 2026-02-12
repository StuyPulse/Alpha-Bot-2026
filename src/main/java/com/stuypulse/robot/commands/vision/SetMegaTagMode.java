package com.stuypulse.robot.commands.vision;

import com.stuypulse.robot.subsystems.vision.LimelightVision;
import com.stuypulse.robot.subsystems.vision.LimelightVision.MegaTagMode;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetMegaTagMode extends InstantCommand {
    
    private MegaTagMode megaTagMode;

    public SetMegaTagMode(MegaTagMode mode) {
        this.megaTagMode = mode;
    }

    @Override
    public void initialize() {
        super.initialize();
        LimelightVision.getInstance().setMegaTagMode(megaTagMode);
    }
}
