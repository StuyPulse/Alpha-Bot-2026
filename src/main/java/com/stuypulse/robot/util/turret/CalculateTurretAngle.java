package com.stuypulse.robot.util.turret;

import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Settings;

import edu.wpi.first.math.geometry.Rotation2d;

public class CalculateTurretAngle {

    private static final double MAX_ANGLE_DEGREES = Settings.Turret.MAX_THEORETICAL_ROTATION.getDegrees();
    private static final double MIN_ANGLE_DEGREES = Settings.Turret.MIN_THEORETICAL_ROTATION.getDegrees();
    private static final double RESOLUTION = Settings.Turret.RESOLUTION_OF_ABSOLUTE_ENCODER;
    private static final int NUM_POINTS = (int) ((MAX_ANGLE_DEGREES - MIN_ANGLE_DEGREES) / RESOLUTION);

    private static final double[] mechanismAngles = new double[NUM_POINTS];
    private static final double[] ARRAY_17T = generateEncoderValues(17);
    private static final double[] ARRAY_18T = generateEncoderValues(18);

    private static double[] generateEncoderValues(int teeth) {
        double[] values = new double[NUM_POINTS];
        double gearRatio = Constants.Turret.BigGear.TEETH/ teeth;
        int i = 0;

        for (double angle = MIN_ANGLE_DEGREES; angle < MAX_ANGLE_DEGREES; angle += RESOLUTION) {
            mechanismAngles[i] = angle;
            values[i] = (angle * gearRatio) % 360.0;
            i++;
        }

        return values;
    }

    public static Rotation2d getAbsoluteAngle(double encoder17TValue, double encoder18TValue) {
        int leastDistanceIndex = 0;
        double leastDistance = Double.MAX_VALUE;

        for (int i = 0; i < NUM_POINTS; i++) {
            double distance = Math.abs(encoder17TValue - ARRAY_17T[i])
                    + Math.abs(encoder18TValue - ARRAY_18T[i]);
            if (distance < leastDistance) {
                leastDistance = distance;
                leastDistanceIndex = i;
            }
        }

        return Rotation2d.fromDegrees(mechanismAngles[leastDistanceIndex]);
    }
}
