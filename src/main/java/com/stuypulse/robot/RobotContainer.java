/************************ PROJECT ALPHABOT ************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot;

import com.stuypulse.robot.commands.auton.DoNothingAuton;
import com.stuypulse.robot.commands.feeder.FeederFeed;
import com.stuypulse.robot.commands.feeder.FeederStop;
import com.stuypulse.robot.commands.hoodedshooter.HoodedShooterShoot;
import com.stuypulse.robot.commands.hoodedshooter.HoodedShooterStow;
import com.stuypulse.robot.commands.spindexer.SpindexerRun;
import com.stuypulse.robot.commands.spindexer.SpindexerStop;
import com.stuypulse.robot.commands.swerve.SwerveDriveDrive;
import com.stuypulse.robot.commands.turret.TurretIdle;
import com.stuypulse.robot.commands.turret.TurretShoot;
import com.stuypulse.robot.constants.Field;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.subsystems.feeder.Feeder;
import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter;
import com.stuypulse.robot.subsystems.hoodedshooter.hood.Hood;
import com.stuypulse.robot.subsystems.hoodedshooter.shooter.Shooter;
import com.stuypulse.robot.subsystems.intake.Intake;
import com.stuypulse.robot.subsystems.spindexer.Spindexer;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.subsystems.turret.Turret;
import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.AutoGamepad;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class RobotContainer {

    // Gamepads
    public final Gamepad driver = new AutoGamepad(Ports.Gamepad.DRIVER);
    
    // Subsystem
    public final CommandSwerveDrivetrain swerve = CommandSwerveDrivetrain.getInstance();
    public final HoodedShooter hoodedshooter = HoodedShooter.getInstance();
    public final Hood hood = Hood.getInstance();
    public final Shooter shooter = Shooter.getInstance();
    
    public final Spindexer spindexer = Spindexer.getInstance();
    public final Feeder feeder = Feeder.getInstance();
    public final HoodedShooter hoodedShooter = HoodedShooter.getInstance();
    public final Intake intake = Intake.getInstance();
    public final Turret turret = Turret.getInstance();

    // Autons
    private static SendableChooser<Command> autonChooser = new SendableChooser<>();

    // Robot container

    public RobotContainer() {
        swerve.configureAutoBuilder();
        configureDefaultCommands();
        configureButtonBindings();
        configureAutons();

        SmartDashboard.putData("Field", Field.FIELD2D);
    }

    /****************/
    /*** DEFAULTS ***/
    /****************/

    private void configureDefaultCommands() {
        swerve.setDefaultCommand(new SwerveDriveDrive(driver));
        //hoodedshooter.setDefaultCommand(new TurretHoodAlignToTarget());
    }   

    /***************/
    /*** BUTTONS ***/
    /***************/

    private void configureButtonBindings() {
        
        // driver.getBottomButton()
        //     .whileTrue(new IntakeIntake())
        //     .onFalse(new IntakeStop());
        // // driver.getLeftButton().onTrue(new IntakeStop());

        driver.getLeftButton().onTrue(new SpindexerRun());
        driver.getRightButton().onTrue(new SpindexerStop());

        driver.getBottomButton().onTrue(new FeederFeed());
        driver.getTopButton().onTrue(new FeederStop());

        driver.getDPadUp().onTrue(new HoodedShooterShoot());
        driver.getDPadDown().onTrue(new HoodedShooterStow());

        driver.getRightTriggerButton().onTrue(new TurretShoot());
        driver.getLeftTriggerButton().onTrue(new TurretIdle());

        // driver.getDPadDown()
        //     .onTrue(new HoodedShooterShoot())
        //     .onFalse(new HoodedShooterStow());

        // driver.getDPadUp()
        //     .onTrue(new HoodedShooterFerry())
        //     .onFalse(new HoodedShooterStow());

        // driver.getDPadUp().whileTrue(new HoodedShooterShoot()
        //     .alongWith(new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())
        //     .andThen(new FeederFeed())))
        // .onFalse(new HoodedShooterStow()
        //     .alongWith(new FeederStop()));

    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        autonChooser.setDefaultOption("Do Nothing", new DoNothingAuton());
        // autonChooser.addOption("SysID Module Translation Dynamic Forward", swerve.sysIdDynamic(Direction.kForward));
        // autonChooser.addOption("SysID Module Translation Dynamic Backwards", swerve.sysIdDynamic(Direction.kReverse));
        // autonChooser.addOption("SysID Module Translation Quasi Forwards", swerve.sysIdQuasistatic(Direction.kForward));
        // autonChooser.addOption("SysID Module Translation Quasi Backwards", swerve.sysIdQuasistatic(Direction.kReverse));

        // autonChooser.addOption("SysID Module Rotation Dynamic Forwards", swerve.sysIdRotDynamic(Direction.kForward));
        // autonChooser.addOption("SysID Module Rotation Dynamic Backwards", swerve.sysIdRotDynamic(Direction.kReverse));
        // autonChooser.addOption("SysID Module Rotation Quasi Forwards", swerve.sysIdRotQuasi(Direction.kForward));
        // autonChooser.addOption("SysID Module Rotation Quasi Backwards", swerve.sysIdRotQuasi(Direction.kReverse));

        // SysIdRoutine shooterSysId = shooter.getShooterSysIdRoutine();
        // autonChooser.addOption("SysID Shooter Dynamic Forward", shooterSysId.dynamic(Direction.kForward));
        // autonChooser.addOption("SysID Shooter Dynamic Backwards", shooterSysId.dynamic(Direction.kReverse));
        // autonChooser.addOption("SysID Shooter Quasi Forwards", shooterSysId.quasistatic(Direction.kForward));
        // autonChooser.addOption("SysID Shooter Quasi Backwards", shooterSysId.quasistatic(Direction.kReverse));

        // SysIdRoutine hoodSysId = hood.getHoodSysIdRoutine();
        // autonChooser.addOption("SysID Hood Dynamic Forward", hoodSysId.dynamic(Direction.kForward));
        // autonChooser.addOption("SysID Hood Dynamic Backwards", hoodSysId.dynamic(Direction.kReverse));
        // autonChooser.addOption("SysID Hood Quasi Forwards", hoodSysId.quasistatic(Direction.kForward));
        // autonChooser.addOption("SysID Hood Quasi Backwards", hoodSysId.quasistatic(Direction.kReverse));

        // SysIdRoutine spindexerSysId = spindexer.getSysIdRoutine();
        // autonChooser.addOption("SysID Spinny Boi Dynamic Forward", spindexerSysId.dynamic(Direction.kForward));
        // autonChooser.addOption("SysID Spinny Boi Dynamic Backwards", spindexerSysId.dynamic(Direction.kReverse));
        // autonChooser.addOption("SysID Spinny Boi Quasi Forwards", spindexerSysId.quasistatic(Direction.kForward));
        // autonChooser.addOption("SysID Spinny Boi Quasi Backwards", spindexerSysId.quasistatic(Direction.kReverse));
   
        // SysIdRoutine feederSysId = feeder.getSysIdRoutine();
        // autonChooser.addOption("SysID Feed Dynamic Forward", feederSysId.dynamic(Direction.kForward));
        // autonChooser.addOption("SysID Feed Dynamic Backwards", feederSysId.dynamic(Direction.kReverse));
        // autonChooser.addOption("SysID Feed Quasi Forwards", feederSysId.quasistatic(Direction.kForward));
        // autonChooser.addOption("SysID Feed Quasi Backwards", feederSysId.quasistatic(Direction.kReverse));

        SmartDashboard.putData("Autonomous", autonChooser);
    }

    public Command getAutonomousCommand() {
        return autonChooser.getSelected();
    }
}
