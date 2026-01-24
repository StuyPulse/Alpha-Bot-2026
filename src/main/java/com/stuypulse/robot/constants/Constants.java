package com.stuypulse.robot.constants;

public interface Constants {
    public interface Turret {
        public interface Encoder18t {
            public final int teeth = 18;
            public final double offset = 0; // TODO: FIND OFFSET
        }
        
        public interface Encoder17t {
            public final int teeth = 17;
            public final double offset = 0; // TODO: FIND OFFSET
        }

        public interface BigGear {
            public final int teeth = 84;
        }
    }
}