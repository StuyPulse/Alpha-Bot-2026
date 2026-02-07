package com.stuypulse.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public interface Constants {
    public interface HoodedShooter {
        Rotation2d MIN_ANGLE = Rotation2d.fromDegrees(5);
        Rotation2d MAX_ANGLE = Rotation2d.fromDegrees(40);

        double SHOT_RPM = 4000;
        double FERRY_RPM = 4000;
        Transform2d TURRET_OFFSET = new Transform2d(0, 0, Rotation2d.kZero);  // TODO: add the field relative shooter offset on the robot


        public interface Hood {
            public final double GEAR_RATIO = 1.0; // TODO: find gear ratio
        }

        public interface Shooter {
            public final double GEAR_RATIO = 1.0; // TODO: find gear ratio
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

        public final double GEAR_RATIO_MOTOR_TO_MECH = 1425.0 / 36.0;
    }

    
    public interface Feeder {
        public final double GEAR_RATIO = 0.0; 
    }
}