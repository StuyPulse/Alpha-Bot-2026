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
        int LEADER_KRAKEN = 20;
        int FOLLOWER_KRAKEN = 21;
    }

    public interface HoodedShooter {
        public interface Hood {
            int HoodMotor = 30;
        }

        public interface Shooter {
            final int ShooterMotorLead = 40;
            final int ShooterMotorFollower = 41;
        }
    }

    public interface Turret {
        // TODO: Get Ports
        int MOTOR = 0;
        int ENCODER17T = 0;
        int ENCODER18T = 0;
    }

    public interface Feeder {
        int FEEDER_MOTOR = 0; //TODO: change to actual port
    }
}
