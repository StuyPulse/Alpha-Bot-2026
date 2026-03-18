/************************ PROJECT ALPHA *************************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved. */
/* Use of this source code is governed by an MIT-style license */
/* that can be found in the repository LICENSE file.           */
/***************************************************************/
package com.stuypulse.robot.constants;

import com.pathplanner.lib.config.PIDConstants;
import com.stuypulse.stuylib.network.SmartNumber;

public class Gains {
    public interface Swerve {
        public interface Drive {
            double kS = 0.0;
            double kV = 0.124;
            double kA = 0.0;
            double kP = 0.1;
            double kI = 0.0;
            double kD = 0.0;
        }

        public interface Turn {
            double kS = 0.1;
            double kV = 2.66;
            double kA = 0.0;
            double kP = 100.0;
            double kI = 0.0;
            double kD = 0.5;
        }

        public interface Alignment {
            public interface Rotation {  
                double kp = 112.3;
                double ki = 0.0;
                double kd = 2.3758;
                double ks = 0.31395;
                double kv = 0.10969;
                double ka = 0.026589;
            }

            double kP = 0.0;
            double kI = 0.0;
            double kD = 0.0;
            double akP = 0.0;
            double akI = 0.0;
            double akD = 0.0;

            PIDConstants XY = new PIDConstants(0.0, 0.0, 0.0);
            PIDConstants THETA = new PIDConstants(0.0, 0.0, 0.0);
        }
    }
    public interface HoodedShooter {
        public interface Shooter {
            double kP = 0.45;
            double kI = 0.0;
            double kD = 0.0;

            double kS = 0.0;
            double kV = 0.125;
            double kA = 0.0;

        // double FOC_kP =  0.0;// 10.5;
        // double FOC_kI = 0.0;
        // double FOC_kD = 0.0;
        
        // double FOC_kS = 4.4;//10.320099;
        // double FOC_kA = 0.0;
        // double FOC_kV = 0.0;
        // 0.0835186;
        //0.5;

        //PHOENIX 161 METHOD:
        // SmartNumber FOC_kP = new SmartNumber("Shooter/Moveable Gains/kP", 0.00231481);// 10.5;
        // SmartNumber FOC_kI = new SmartNumber("Shooter/Moveable Gains/kI", 0.0);
        // SmartNumber FOC_kD = new SmartNumber("Shooter/Moveable Gains/kD", 0.0);
        // SmartNumber FOC_kS = new SmartNumber("Shooter/Moveable Gains/kS",  4.3);//10.320099;
        // SmartNumber FOC_kA = new SmartNumber("Shooter/Moveable Gains/kA", 0.0);
        // SmartNumber FOC_kV = new SmartNumber("Shooter/Moveable Gains/kV",  0.060000); //0.0835186;


        //DISCORD INTERN METHOD: WITH FLYWHEEL (GOOD)
        // SmartNumber FOC_kP = new SmartNumber("Shooter/Moveable Gains/kP", 5.0);
        // SmartNumber FOC_kI = new SmartNumber("Shooter/Moveable Gains/kI", 0.0);
        // SmartNumber FOC_kD = new SmartNumber("Shooter/Moveable Gains/kD", 0.3);
        // SmartNumber FOC_kS = new SmartNumber("Shooter/Moveable Gains/kS",  6.707);
        // SmartNumber FOC_kA = new SmartNumber("Shooter/Moveable Gains/kA", 0.0);
        // SmartNumber FOC_kV = new SmartNumber("Shooter/Moveable Gains/kV",  0.04315304948);

        //DISCORD INTERN METHOD: NO FLYWHEEL (NOT DONE)
        SmartNumber FOC_kP = new SmartNumber("Shooter/Moveable Gains/kP", 13); //18.5 (really good with the smoothed out graph (check motor configs))
        SmartNumber FOC_kI = new SmartNumber("Shooter/Moveable Gains/kI", 0.0);
        SmartNumber FOC_kD = new SmartNumber("Shooter/Moveable Gains/kD", 0.0); //0.4 (really good with the smoothed out graph (check motor configs))
        SmartNumber FOC_kS = new SmartNumber("Shooter/Moveable Gains/kS",  5.81291074);
        SmartNumber FOC_kA = new SmartNumber("Shooter/Moveable Gains/kA", 0.0);
        SmartNumber FOC_kV = new SmartNumber("Shooter/Moveable Gains/kV",  0.0264243);

        //HIGH VEL AVERAGE: 60.44 rps
        //LOW VEL AVERAGE: 11.243 rps

        //HIGH AMP AVERAGE: 7.41
        //LOW AMP AVERAGE: 6.11 

        // 1.3/49.197

        //TODO: test bang bang in seperate thread

        }

        public interface Hood {
            double kP = 400.0; //1200.0
            double kI = 0.0;
            double kD = 0.0; //TODO: ADD MORE KD

            double kS = 0.28; //found .45 from tests
            double kV = 0.0;
            double kA = 0.0;
        }   
    }

    public interface Spindexer {
        double kP = 1.20;
        double kI = 0.0;
        double kD = 0.0;
        
        double kS = 0.019444;
        double kA = 0.010876;
        double kV = 0.38546;

    }

    public interface Feeder {
        SmartNumber kS = new SmartNumber("Feeder/kS", 0.1728);
        SmartNumber kA = new SmartNumber("Feeder/kA", 0.0028428);
        SmartNumber kV = new SmartNumber("Feeder/kV", 0.11725);

        SmartNumber kP = new SmartNumber("Feeder/kP", 0.00015508);
        SmartNumber kI = new SmartNumber("Feeder/kI", 0.0);
        SmartNumber kD = new SmartNumber("Feeder/kD", 0.0);
    }

    public interface Turret {
        double kS = 0.179;
        double kA = 0.0; //0.20;

        //safe gains commented out - POSITION CONTROL ONLY
        double kP = 1300.0; //25.0;
        double kI = 0.0;
        double kD = 140.0; //3.0;
    }
}
