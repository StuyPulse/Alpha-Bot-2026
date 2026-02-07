/************************ PROJECT PHIL ************************/
/* Copyright (c) 2024 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot.constants;

import com.ctre.phoenix6.CANBus;

/** This file contains the different ports of motors, solenoids and sensors */
public interface Ports {
    // TODO: Get bus name
    public CANBus bus = new CANBus("rio");

    public interface Gamepad {
        int DRIVER = 0;
        int OPERATOR = 1;
        int DEBUGGER = 2;
    }

    public interface Spindexer {
        int MOTOR_LEADER = 16;
        int MOTOR_FOLLOW = 30;
    }

    public interface HoodedShooter {
        public interface Hood {
            int MOTOR = 25;
            int THROUGHBORE_ENCODER = 37;
        }

        public interface Shooter {
            int MOTOR_LEAD = 17;
            int MOTOR_FOLLOW = 14;
        }
    }

    public interface Turret {
        int MOTOR = 50;
        int ENCODER17T = 38;
        int ENCODER18T = 21;
    }

    public interface Feeder {
        int MOTOR = 15;
    }

    public interface Intake {
        int MOTOR_LEAD = 20;
        int MOTOR_FOLLOW = 51;
    }
}
