package com.stuypulse.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;

import com.stuypulse.stuylib.network.SmartNumber;

import edu.wpi.first.math.geometry.Transform2d;

public interface Constants {
    public interface HoodedShooter {
        Transform2d TURRET_OFFSET = new Transform2d(0, 0, Rotation2d.kZero);

        public interface Hood {
            public final double GEAR_RATIO = 1290300.0 / 5967.0; 
            public final Rotation2d MIN_ANGLE = Rotation2d.fromDegrees(60);
            public final Rotation2d MAX_ANGLE = Rotation2d.fromDegrees(315);

            public final Rotation2d ENCODER_OFFSET = new Rotation2d();
        }
        public interface Shooter {
            public final double GEAR_RATIO = 1.0;
        }
    }
  
    public interface Align {
        int MAX_ITERATIONS = 5;
        double TIME_TOLERANCE = 0.01;
    }

    public interface Spindexer {
        public final double GEAR_RATIO = 40.0 / 12.0;
    }

    public interface Turret {
        public interface Encoder18t {
            public final int TEETH = 18;
            public final Rotation2d OFFSET = new Rotation2d();
        }
        
        public interface Encoder17t {
            public final int TEETH = 17;
            public final Rotation2d OFFSET = new Rotation2d();
        }

        public interface BigGear {
            public final int TEETH = 95;
        }

        public interface SoftwareLimit {
            public final double FORWARD_MAX_ROTATIONS = 1.5;
            public final double BACKWARDS_MAX_ROTATIONS = 1.5;
        } 

        public final double GEAR_RATIO_MOTOR_TO_MECH = 1425.0 / 36.0;
    }

    
    public interface Feeder {
        public final double GEAR_RATIO = 1.0; 
    }
}