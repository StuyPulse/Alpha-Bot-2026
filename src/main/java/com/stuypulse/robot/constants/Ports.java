/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.constants;

import com.ctre.phoenix6.CANBus;

/** This file contains the different ports of motors, solenoids and sensors */
public interface Ports {
    // TODO: Get bus name
    public CANBus bus = new CANBus("rio");

    public interface Gamepad {
        int DRIVER = 0;
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
