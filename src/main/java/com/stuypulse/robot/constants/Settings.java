/************************ PROJECT PHIL ************************/
/* Copyright (c) 2024 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot.constants;

import com.pathplanner.lib.path.PathConstraints;
import com.stuypulse.stuylib.network.SmartBoolean;
import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

/*-
 * File containing tunable settings for every subsystem on the robot.
 *
 * We use StuyLib's SmartNumber / SmartBoolean in order to have tunable
 * values that we can edit on Shuffleboard.
 */
public interface Settings {
    double DT = 0.020;
    boolean DEBUG_MODE = true;

    public interface EnabledSubsystems {
        SmartBoolean SWERVE = new SmartBoolean("Enabled Subsystems/Swerve Is Enabled", false);
        SmartBoolean TURRET = new SmartBoolean("Enabled Subsystems/Turret Is Enabled", false);
        SmartBoolean HOODED_SHOOTER = new SmartBoolean("Enabled Subsystems/Hooded Shooter Is Enabled", false);
        SmartBoolean SHOOTER = new SmartBoolean("Enabled Subsystems/Shooter Is Enabled", false);
        SmartBoolean HOOD = new SmartBoolean("Enabled Subsystems/Hood Is Enabled", true);
        SmartBoolean FEEDER = new SmartBoolean("Enabled Subsystems/Feeder Is Enabled", false);
        SmartBoolean SPINDEXER = new SmartBoolean("Enabled Subsystems/Spindexer Is Enabled", false);
        SmartBoolean LIMELIGHT = new SmartBoolean("Enabled Subsystems/Limelight Is Enabled", false);
        SmartBoolean INTAKE = new SmartBoolean("Enabled Subsystems/Intake Is Enabled", false);
    }

    public interface Spindexer {
        double RUNNING_SPEED = 6000.0;
        double STOP_SPEED = 0.0;
    }

    public interface Feeder {
        double FEEDER_STOP = 0.0;
        double FEEDER_MAX = 5000.0; 
        double FEEDER_REVERSE = -500.0;
        public final SmartNumber FEED_RPM = new SmartNumber("Feeder/RPM override", 6000);

    }

    public interface Intake {
        double INTAKE_SPEED = 1.0;
        double OUTTAKE_SPEED = -1.0;
        double STOP = 0.0;
    }
    public interface HoodedShooter {

        SmartNumber SHOOT_RPM = new SmartNumber("HoodedShooter/Shoot State Target RPM", 3000.0);
        SmartNumber FERRY_RPM = new SmartNumber("HoodedShooter/Ferry State Target RPM", 2000.0);

        double SHOOTER_TOLERANCE_RPM = 150.0;
        double HOOD_TOLERANCE_DEG = 5.0;
        public interface ShooterRPMS {
            public final double REVERSE = -0.0;
            public final double HUB_RPM = 0.0; 
            public final double LEFT_CORNER_RPM = 0.0; // TBD
            public final double RIGHT_CORNER_RPM = 0.0; // TBD
            public final double STOW = 0.0; // TBD
        }

        public interface ShooterRPMDistances {
            public final double RPM1Distance = 0.0;
            public final double RPM2Distance = 0.0;
            public final double RPM3Distance = 0.0;
        }
    }

    public interface Turret {
        Rotation2d MAX_VEL = new Rotation2d(Units.degreesToRadians(600.0));
        Rotation2d MAX_ACCEL = new Rotation2d(Units.degreesToRadians(600.0));        
        double TOLERANCE_DEG = 2.0;

        Rotation2d HUB = Rotation2d.fromDegrees(0.0);
        Rotation2d LEFT_CORNER = Rotation2d.fromDegrees(0.0);
        Rotation2d RIGHT_CORNER = Rotation2d.fromDegrees(0.0);

    }

    public interface Swerve {
        double MODULE_VELOCITY_DEADBAND_M_PER_S = 0.1;
        double ROTATIONAL_DEADBAND_RAD_PER_S = 0.1;
        
        public interface Constraints {    
            double MAX_VELOCITY_M_PER_S = 4.3;
            double MAX_ACCEL_M_PER_S_SQUARED = 15.0;
            double MAX_ANGULAR_VEL_RAD_PER_S = Units.degreesToRadians(400.0);
            double MAX_ANGULAR_ACCEL_RAD_PER_S = Units.degreesToRadians(900.0);
    
            PathConstraints DEFAULT_CONSTRAINTS =
                new PathConstraints(
                    MAX_VELOCITY_M_PER_S,
                    MAX_ACCEL_M_PER_S_SQUARED,
                    MAX_ANGULAR_VEL_RAD_PER_S,
                    MAX_ANGULAR_ACCEL_RAD_PER_S);
        }

        public interface Alignment {
            public interface Constraints {
                double DEFAULT_MAX_VELOCITY = 4.3;
                double DEFAULT_MAX_ACCELERATION = 15.0;
                double DEFUALT_MAX_ANGULAR_VELOCITY = Units.degreesToRadians(400.0);
                double DEFAULT_MAX_ANGULAR_ACCELERATION = Units.degreesToRadians(900.0);
            }

            public interface Tolerances {
                double X_TOLERANCE = Units.inchesToMeters(2.0); 
                double Y_TOLERANCE = Units.inchesToMeters(2.0);
                Rotation2d THETA_TOLERANCE = Rotation2d.fromDegrees(2.0);

                Pose2d POSE_TOLERANCE = new Pose2d(
                    Units.inchesToMeters(2.0), 
                    Units.inchesToMeters(2.0), 
                    Rotation2d.fromDegrees(2.0));

                double MAX_VELOCITY_WHEN_ALIGNED = 0.15;

                double ALIGNMENT_DEBOUNCE = 0.15;
            }

            public interface Targets {

            }
        }
    }
    public interface Driver {

        double BUZZ_TIME = 1.0;
        double BUZZ_INTENSITY = 1.0;

        public interface Drive {
            double DEADBAND = 0.05;

            double RC = 0.05;
            double POWER = 2.0;
        }
        public interface Turn {
            double DEADBAND = 0.05;

            double RC = 0.05;
            double POWER = 2.0;
        }
    }
}
