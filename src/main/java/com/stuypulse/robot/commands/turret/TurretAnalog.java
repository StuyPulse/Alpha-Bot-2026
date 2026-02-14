package com.stuypulse.robot.commands.turret;
import java.time.format.TextStyle;

import com.stuypulse.robot.subsystems.turret.Turret;
import com.stuypulse.robot.subsystems.turret.Turret.TurretState;
import com.stuypulse.stuylib.input.Gamepad;

import edu.wpi.first.wpilibj2.command.Command;

public class TurretAnalog extends Command {
    private Gamepad gamepad;
    private Turret turret;
    public TurretAnalog(Gamepad gamepad){
        turret = Turret.getInstance();
        this.gamepad = gamepad;
    }

    @Override 
    public void initialize() {
        super.initialize();
        turret.setState(TurretState.TESTING);
    }
    @Override
    public void execute() {
        super.execute();
        turret.setDriverInput(gamepad);
    }
}
