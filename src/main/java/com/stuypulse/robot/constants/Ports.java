/************************ PROJECT PHIL ************************/
/* Copyright (c) 2024 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot.constants;

import com.ctre.phoenix6.CANBus;

/** This file contains the different ports of motors, solenoids and sensors */
public interface Ports {
    // TODO: Get bus name
    public CANBus bus = new CANBus("bus");

    public interface Gamepad {
        int DRIVER = 0;
        int OPERATOR = 1;
        int DEBUGGER = 2;
    }

    public interface Spindexer {
        int MOTOR_LEADER = 20;
        int MOTOR_FOLLOW = 21;
    }

    public interface HoodedShooter {
        public interface Hood {
            int MOTOR = 30;
        }

        public interface Shooter {
            // TODO: Find ports for all motors below
            final int ShooterMotorLead = 40;
            final int ShooterMotorFollower1 = 41; 
            final int ShooterMotorFollower2 = 41;
        }
    }

    public interface Turret {
        int MOTOR = 3;
        int ENCODER17T = 4;
        int ENCODER18T = 5;
    }

    public interface Feeder {
        int MOTOR = 6;
    }

    public interface Intake {
        int MOTOR_LEAD = 7;
        int MOTOR_FOLLOW = 8;
    }
}