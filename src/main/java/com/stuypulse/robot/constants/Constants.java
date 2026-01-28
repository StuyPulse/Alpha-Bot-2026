package com.stuypulse.robot.constants;

public interface Constants {
    public interface HoodedShooter {
        //TODO: GET RATIOS
        public interface Hood {
            public final double GEAR_RATIO = 0.0;
        }

        public interface Shooter {
            public final double GEAR_RATIO = 0.0;
        }
    }

    public interface Spindexer {
        public final double GEAR_RATIO = 40.0 / 12.0; //TODO: RECHECK
    }

    public interface Turret {
        public interface Encoder18t {
            public final int TEETH = 18;
            public final double OFFSET = 0; // TODO: FIND OFFSET
        }
        
        public interface Encoder17t {
            public final int TEETH = 17;
            public final double OFFSET = 0; // TODO: FIND OFFSET
        }

        public interface BigGear {
            public final int TEETH = 84; //TODO: GET ACTUAL TOOTH AMOUNT
        }

        public final double GEAR_RATIO_MOTOR_TO_MECH = 0.0; //TODO: GET
    }

    public interface Feeder {
        public final double GEAR_RATIO = 0.0; //TODO: GET
    }
}
