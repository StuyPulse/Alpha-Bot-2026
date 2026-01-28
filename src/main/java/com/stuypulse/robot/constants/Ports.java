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
        // TODO: Get Ports
        int MOTOR_LEAD = 20;
        int MOTOR_FOLLOW = 21;
    }

    public interface HoodedShooter {
        public interface Hood {
            int MOTOR = 30;
        }

        public interface Shooter {
            int MOTOR_LEAD = 40;
            int MOTOR_FOLLOW = 41;
        }
    }

    public interface Turret {
        // TODO: Get Ports
        int MOTOR = 0;
        int ENCODER17T = 0;
        int ENCODER18T = 0;
    }

    public interface Feeder {
        int MOTOR = 0; //TODO: GET PORTS
    }

    public interface Intake {
        int MOTOR_LEAD = 0; //TODO: GET PORTS
        int MOTOR_FOLLOW = 0;
    }
}