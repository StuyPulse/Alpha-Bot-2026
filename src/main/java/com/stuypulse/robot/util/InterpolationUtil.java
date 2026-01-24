package com.stuypulse.robot.util;

import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.constants.Settings.AngleInterpolationValue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class InterpolationUtil {
    private static final InterpolatingDoubleTreeMap interpolator1;
    private static final InterpolatingDoubleTreeMap interpolator2;
    private static final InterpolatingDoubleTreeMap interpolator3;

    static {
        interpolator1 = new InterpolatingDoubleTreeMap();
        interpolator2 = new InterpolatingDoubleTreeMap();
        interpolator3 = new InterpolatingDoubleTreeMap();

        for (AngleInterpolationValue interpolationvalue : Settings.HoodedShooter.interpolator1data) {
            interpolator1.put(interpolationvalue.getdouble(), interpolationvalue.getAngle().getDegrees());
        }

        for (AngleInterpolationValue interpolationvalue : Settings.HoodedShooter.interpolator2data) {
            interpolator2.put(interpolationvalue.getdouble(), interpolationvalue.getAngle().getDegrees());
        }

        for (AngleInterpolationValue interpolationvalue : Settings.HoodedShooter.interpolator3data) {
            interpolator3.put(interpolationvalue.getdouble(), interpolationvalue.getAngle().getDegrees());
        }
    }

    public static AngleInterpolationValue getHoodAngleInterpolation(double distance) {
        if (Settings.HoodedShooter.ShooterRPMDistances.RPM1Distance > distance) {
            return new AngleInterpolationValue(Settings.HoodedShooter.ShooterRPMS.RPM1, Rotation2d.fromDegrees(interpolator1.get(distance)));
        } if (Settings.HoodedShooter.ShooterRPMDistances.RPM2Distance < distance) {
            return new AngleInterpolationValue(Settings.HoodedShooter.ShooterRPMS.RPM3, Rotation2d.fromDegrees(interpolator3.get(distance)));
        } else {
            return new AngleInterpolationValue(Settings.HoodedShooter.ShooterRPMS.RPM2, Rotation2d.fromDegrees(interpolator2.get(distance)));
        }
    }

}
