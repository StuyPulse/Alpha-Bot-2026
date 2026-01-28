/************************ PROJECT PHIL ************************/
/* Copyright (c) 2024 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/

package com.stuypulse.robot.constants;

/** This file contains the different ports of motors, solenoids and sensors */
public interface Ports {
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

    public interface Intake {
        //TODO: Get Ports
        int LEADER_INTAKE_NEO = 41; //Placeholder
        int FOLLOWER_INTAKE_NEO = 42; //Placeholder
    }
}