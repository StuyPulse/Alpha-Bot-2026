/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot;

import com.stuypulse.stuylib.input.Gamepad;
import com.stuypulse.stuylib.input.gamepads.AutoGamepad;

import com.stuypulse.robot.commands.auton.Box;
import com.stuypulse.robot.commands.auton.DoNothingAuton;
import com.stuypulse.robot.commands.auton.StraightLine;
import com.stuypulse.robot.commands.auton.TwoCycleBottom;
import com.stuypulse.robot.commands.auton.TwoCycleTop;
import com.stuypulse.robot.commands.feeder.FeederFeed;
import com.stuypulse.robot.commands.feeder.FeederStop;
import com.stuypulse.robot.commands.hoodedshooter.HoodedShooterShoot;
import com.stuypulse.robot.commands.hoodedshooter.HoodedShooterStow;
import com.stuypulse.robot.commands.spindexer.SpindexerRun;
import com.stuypulse.robot.commands.spindexer.SpindexerStop;
import com.stuypulse.robot.commands.swerve.SwerveDriveDrive;
import com.stuypulse.robot.commands.swerve.SwerveResetHeading;
import com.stuypulse.robot.commands.turret.TurretFerry;
import com.stuypulse.robot.commands.turret.TurretIdle;
import com.stuypulse.robot.commands.turret.TurretSeed;
import com.stuypulse.robot.commands.turret.TurretShoot;
import com.stuypulse.robot.commands.turret.TurretZero;
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
import com.stuypulse.robot.subsystems.vision.LimelightVision;
import com.stuypulse.robot.util.PathUtil.AutonConfig;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class RobotContainer {

    // Gamepads
    public final Gamepad driver = new AutoGamepad(Ports.Gamepad.DRIVER);
    
    // Subsystem
    public final CommandSwerveDrivetrain swerve = CommandSwerveDrivetrain.getInstance();
    public final HoodedShooter hoodedshooter = HoodedShooter.getInstance();
    public final Hood hood = Hood.getInstance();
    public final Shooter shooter = Shooter.getInstance();
    public final LimelightVision limelight = LimelightVision.getInstance();
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
        // configureAutons();

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

        driver.getRightTriggerButton().onTrue(new TurretShoot());

        driver.getLeftTriggerButton().onTrue(new TurretFerry());

        driver.getDPadDown()
            .onTrue(new TurretSeed());

        driver.getDPadUp()
            .onTrue(new SwerveResetHeading());
            
        // driver.getDPadUp().onTrue(new TurretAnalog(driver));

        // SCORING ROUTINE
        driver.getTopButton()
            .whileTrue(new TurretZero()
                .alongWith(new HoodedShooterShoot())
                    .alongWith(new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance()))
                    .andThen(new FeederFeed().onlyIf(() -> hoodedShooter.isShooterAtTolerance())
                        .alongWith(new WaitUntilCommand(() -> feeder.atTolerance()))
                            .andThen(new SpindexerRun().onlyIf(() -> hoodedShooter.isShooterAtTolerance()))
                    )
            )
            .onFalse(new SpindexerStop()
                // .alongWith(new HoodedShooterStow())
                .alongWith(new FeederStop()));

        driver.getBottomButton()
            .onTrue(new TurretShoot());

        // driver.getDPadRight()
        //     .whileTrue(
        //         new SwerveXMode().alongWith(
        //             new HoodedShooterShoot().alongWith(
        //                 new TurretShoot()).alongWith(
        //                     new WaitUntilCommand(() -> hoodedShooter.isHoodAtTolerance())).alongWith(
        //                     new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())).alongWith(
        //                     new WaitUntilCommand(() -> turret.atTargetAngle())).andThen(
        //                         new SpindexerRun().alongWith(new FeederFeed()))))
        //     .onFalse(
        //         new HoodedShooterStow().alongWith(
        //         new TurretHoodAlignToTarget().alongWith(
        //         new SpindexerRun().alongWith(
        //         new FeederStop())))
        //     );

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

//-------------------------------------------------------------------------------------------------------------------------\\
//-------------------------------------------------------------------------------------------------------------------------\\
//-------------------------------------------------------------------------------------------------------------------------\\
//-------------------------------------------------------------------------------------------------------------------------\\
//-------------------------------------------------------------------------------------------------------------------------\\

        /**
        // Climb Align
        driver.getTopButton()
            .whileTrue(SwerveClimbAlign());

        // Left Corner Shoot
        driver.getLeftButton()
            .whileTrue(
                new SwerveXMode().alongWith(
                    new HoodedShooterLeftCorner().alongWith(
                        new TurretLeftCorner()).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isHoodAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> turret.atTargetAngle())).andThen(
                                new SpindexerRun().alongWith(new FeederFeed()))))
            .onFalse(
                new HoodedShooterStow().alongWith(
                new TurretHoodAlignToTarget().alongWith(
                new SpindexerRun().alongWith(
                new FeederStop())))
            );

        // Right Corner Shoot
        driver.getRightButton()
            .whileTrue(
                new SwerveXMode().alongWith(
                    new HoodedShooterRightCorner().alongWith(
                        new TurretRightCorner()).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isHoodAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> turret.atTargetAngle())).andThen(
                                new SpindexerRun().alongWith(new FeederFeed()))))
            .onFalse(
                new HoodedShooterStow().alongWith(
                new TurretHoodAlignToTarget().alongWith(
                new SpindexerRun().alongWith(
                new FeederStop())))
            );

        // Hub Shoot
        driver.getBottomButton()
            .whileTrue(
                new SwerveXMode().alongWith(
                    new HoodedShooterHub().alongWith(
                        new TurretHub()).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isHoodAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> turret.atTargetAngle())).andThen(
                                new SpindexerRun().alongWith(new FeederFeed()))))
            .onFalse(
                new HoodedShooterStow().alongWith(
                new TurretHoodAlignToTarget().alongWith(
                new SpindexerRun().alongWith(
                new FeederStop())))
            );

        // Intake On
        driver.getLeftTriggerButton()
            .onTrue(new IntakeIntake());

        // Intake Off
        driver.getRightTriggerButton()
            .onTrue(new IntakeStop());

        // Climb Down Placeholder
        driver.getLeftBumper()
            .onTrue(new BuzzController(driver));

        // Climb Up Placeholder
        driver.getRightBumper()
            .onTrue(new BuzzController(driver));

        // Reset Heading
        driver.getDPadUp()
            .onTrue(new SwerveResetHeading());

        // Ferry In Place
        driver.getDPadLeft()
            .whileTrue(
                new SwerveXMode().alongWith(
                    new HoodedShooterFerry().alongWith(
                        new TurretFerry()).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isHoodAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> turret.atTargetAngle())).andThen(
                                new SpindexerRun().alongWith(new FeederFeed()))))
            .onFalse(
                new HoodedShooterStow().alongWith(
                new TurretHoodAlignToTarget().alongWith(
                new SpindexerRun().alongWith(
                new FeederStop())))
            );

        // Score In Place
        driver.getDPadRight()
            .whileTrue(
                new SwerveXMode().alongWith(
                    new HoodedShooterShoot().alongWith(
                        new TurretShoot()).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isHoodAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> turret.atTargetAngle())).andThen(
                                new SpindexerRun().alongWith(new FeederFeed()))))
            .onFalse(
                new HoodedShooterStow().alongWith(
                new TurretHoodAlignToTarget().alongWith(
                new SpindexerRun().alongWith(
                new FeederStop())))
            );

        // Unjam
        driver.getDPadDown()
            .whileTrue(
                new HoodedShooterReverse().alongWith(
                    new FeederReverse().alongWith(
                        new IntakeOutake())))
            .onFalse(
                new HoodedShooterStow().alongWith(
                new TurretHoodAlignToTarget().alongWith(
                new SpindexerRun().alongWith(
                new FeederStop().alongWith(
                new IntakeStop()))))
            );

        driver.getLeftMenuButton()
            .onTrue(
                new HoodedShooterFerry().alongWith(
                        new TurretFerry()).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isHoodAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> turret.atTargetAngle())).andThen(
                                new SpindexerRun().alongWith(new FeederFeed())))
            .onFalse(
                new HoodedShooterStow().alongWith(
                new TurretHoodAlignToTarget().alongWith(
                new SpindexerRun().alongWith(
                new FeederStop())))
            );

        driver.getRightMenuButton()
            .onTrue(
                new HoodedShooterFerry().alongWith(
                        new TurretFerry()).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isHoodAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> hoodedShooter.isShooterAtTolerance())).alongWith(
                            new WaitUntilCommand(() -> turret.atTargetAngle())).andThen(
                                new SpindexerRun().alongWith(new FeederFeed())))
            .onFalse(
                new HoodedShooterStow().alongWith(
               new TurretHoodAlignToTarget().alongWith(
                new SpindexerRun().alongWith(
                new FeederStop())))
            );

        **/

    }

    /**************/
    /*** AUTONS ***/
    /**************/

    public void configureAutons() {
        autonChooser.setDefaultOption("Do Nothing", new DoNothingAuton());
        
        AutonConfig STRAIGHT_LINE = new AutonConfig("Straight Line", StraightLine::new, 
            "Straight Line");
        STRAIGHT_LINE.register(autonChooser);

        AutonConfig BOX = new AutonConfig("Box", Box::new, 
            "Box 1", "Box 2", "Box 3", "Box 4");
        BOX.register(autonChooser);

        AutonConfig BOTTOM_TWO_CYCLE = new AutonConfig("Bottom Two Cycle", TwoCycleBottom::new, 
            "Bottom Trench To NZ", "Bottom NZ to Score", "Bottom Score to NZ", "Bottom NZ to Score", "Bottom Trench Score to Tower Right");
        BOTTOM_TWO_CYCLE.register(autonChooser);

        AutonConfig TOP_TWO_CYCLE = new AutonConfig("Top Two Cycle", TwoCycleTop::new, 
            "Top Trench To NZ", "Top NZ to Score", "Top Score to NZ", "Top NZ to Score", "Top Trench Score to Tower Left");
        TOP_TWO_CYCLE.register(autonChooser);

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
