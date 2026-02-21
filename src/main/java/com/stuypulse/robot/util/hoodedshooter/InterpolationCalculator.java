package com.stuypulse.robot.util.hoodedshooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.interpolation.Interpolatable;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import edu.wpi.first.math.interpolation.InverseInterpolator;


public class InterpolationCalculator {

    public static class ShotInformation implements Interpolatable<ShotInformation>{
        private final Rotation2d angleOfHood;
        private final double RPM;
        private final double distanceInMetersToCenterOfHub;

        public ShotInformation(Rotation2d angleOfHood, double RPM, double distanceInMetersToCenterOfHub) {
            this.angleOfHood = angleOfHood;
            this.RPM = RPM;
            this.distanceInMetersToCenterOfHub = distanceInMetersToCenterOfHub;
        }

       @Override
        public ShotInformation interpolate(ShotInformation endValue, double t) {
            return new ShotInformation(
                    new Rotation2d(MathUtil.interpolate(this.angleOfHood.getRadians(), endValue.angleOfHood.getRadians(), t)),
                    MathUtil.interpolate(this.RPM, endValue.RPM, t),
                    MathUtil.interpolate(this.distanceInMetersToCenterOfHub, endValue.distanceInMetersToCenterOfHub, t)
            );
        }
    }

    private InterpolatingTreeMap<Double, ShotInformation> linearInterpolation;
    public InterpolationCalculator() {
        linearInterpolation = new InterpolatingTreeMap<>(InverseInterpolator.forDouble(), ShotInformation::interpolate);
        //linearInterpolation.put(key, value);
    }

    public ShotInformation getInterpolation(double distance) {
        return linearInterpolation.get(distance);
    }

}
