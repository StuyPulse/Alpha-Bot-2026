/************************ PROJECT PHIL ************************/
/* Copyright (c) 2024 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot;

import com.stuypulse.robot.commands.auton.DoNothingAuton;
import com.stuypulse.robot.commands.krakenintake.KrakenIntakeIntake;
import com.stuypulse.robot.commands.krakenintake.KrakenIntakeOuttake;
import com.stuypulse.robot.commands.krakenintake.KrakenIntakeStop;
import com.stuypulse.robot.commands.neointake.NeoIntakeIntake;
import com.stuypulse.robot.commands.neointake.NeoIntakeOuttake;
import com.stuypulse.robot.commands.neointake.NeoIntakeStop;
import com.stuypulse.robot.commands.swerve.SwerveDriveDrive;
import com.stuypulse.robot.commands.swerve.SwerveResetRotation;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.subsystems.krakenintake.KrakenIntake;
import com.stuypulse.robot.subsystems.neointake.NeoIntake;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.AutoGamepad;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class RobotContainer {

    // Gamepads
    public final Gamepad driver = new AutoGamepad(Ports.Gamepad.DRIVER);
    public final Gamepad operator = new AutoGamepad(Ports.Gamepad.OPERATOR);
    
    // Subsystem
    public final CommandSwerveDrivetrain swerve = CommandSwerveDrivetrain.getInstance();
	private final KrakenIntake krakenIntake = KrakenIntake.getInstance();
    private final NeoIntake neoIntake = NeoIntake.getInstance();

    // Autons
    private static SendableChooser<Command> autonChooser = new SendableChooser<>();

    // Robot container

    public RobotContainer() {
        swerve.configureAutoBuilder();
        configureDefaultCommands();
        configureButtonBindings();
        configureAutons();
    }

    /****************/
    /*** DEFAULTS ***/
    /****************/

    private void configureDefaultCommands() {
        swerve.setDefaultCommand(new SwerveDriveDrive(driver));
    }

    /***************/
    /*** BUTTONS ***/
    /***************/

    private void configureButtonBindings() {

        // 1x Kraken Intake
        /*

        driver.getLeftTriggerButton()
            .onTrue(new KrakenIntakeIntake())
            .onFalse(new KrakenIntakeStop());

        driver.getRightTriggerButton()
            .onTrue(new KrakenIntakeOuttake())
            .onFalse(new KrakenIntakeStop());

        */

        // 1x Kraken Intake

        driver.getLeftTriggerButton()
            .onTrue(new NeoIntakeIntake())
            .onFalse(new NeoIntakeStop());

        driver.getRightTriggerButton()
            .onTrue(new NeoIntakeOuttake())
            .onFalse(new NeoIntakeStop());

        driver.getTopButton()
            .onTrue(new SwerveResetRotation());

    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        autonChooser.setDefaultOption("Do Nothing", new DoNothingAuton());

        SmartDashboard.putData("Autonomous", autonChooser);
    }

    public Command getAutonomousCommand() {
        return autonChooser.getSelected();
    }
}
