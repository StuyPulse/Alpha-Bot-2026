package com.stuypulse.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;

public interface Constants {
    public interface HoodedShooter {
        
        public interface Hood {
            public final double GEAR_RATIO = 1290300.0 / 5967.0; // 216.239
            public final double HOOD_ENCODER = 10.0;
        }

        public interface Shooter {
            public final double GEAR_RATIO = 1.0;
        }
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
