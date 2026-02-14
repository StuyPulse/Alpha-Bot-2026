package com.stuypulse.robot.constants;

import com.pathplanner.lib.config.PIDConstants;

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
            double kP = 2.562;
            double kI = 0.0;
            double kD = 0.0;

            double kS = 0.0; //not accurate -3.7335 actual What the banana vro?
            double kV = 0.18976;
            double kA = 0.52537;
        }

        public interface Hood {
            double kP = 110.0; //110.0;
            double kI = 0.0;
            double kD = 0.0;

            double kS = 1.0;
            double kV = 0.5; // 0.5;
            double kA = 0.01; //0.01;
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
        double kP = 0.00015508;
        double kI = 0.0;
        double kD = 0.0;
        
        double kS = 0.1728;
        double kA = 0.0028428;
        double kV = 0.11725;
    }

    public interface Turret {
        double kP = 300.0;
        double kI = 0.0;
        double kD = 58.0;
    }
}
