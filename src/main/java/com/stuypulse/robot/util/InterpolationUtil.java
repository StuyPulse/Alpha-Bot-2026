package com.stuypulse.robot.util;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.util.AngleRPMPair;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class InterpolationUtil {
    private static final InterpolatingDoubleTreeMap interpolator1;

    static {
        interpolator1 = new InterpolatingDoubleTreeMap();

        for (AngleRPMPair interpolationvalue : Settings.HoodedShooter.interpolator1data) {
            interpolator1.put(interpolationvalue.getdouble(), interpolationvalue.getAngle().getDegrees());
        }
    }

    public static AngleRPMPair getHoodAngleInterpolation(double distance) {
        if (Settings.HoodedShooter.ShooterRPMDistances.RPM1Distance > distance) {
            return new AngleRPMPair(Settings.HoodedShooter.ShooterRPMS.RPM1, Rotation2d.fromDegrees(interpolator1.get(distance)));
        } if (Settings.HoodedShooter.ShooterRPMDistances.RPM2Distance < distance) {
            return new AngleRPMPair(Settings.HoodedShooter.ShooterRPMS.RPM3, Rotation2d.fromDegrees(interpolator1.get(distance)));
        } else {
            return new AngleRPMPair(Settings.HoodedShooter.ShooterRPMS.RPM2, Rotation2d.fromDegrees(interpolator1.get(distance)));
        }
    }

}
