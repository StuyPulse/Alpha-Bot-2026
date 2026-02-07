package com.stuypulse.robot.subsystems.hoodedshooter;

import com.stuypulse.robot.constants.Gains;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.stuylib.control.angle.feedback.AnglePIDController;
import com.stuypulse.stuylib.control.feedback.PIDController;
import com.stuypulse.stuylib.math.Angle;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.*;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj.simulation.LinearSystemSim;

public class HoodedShooterSim extends HoodedShooter{
    private final LinearSystemSim<N2, N1, N2> hood;
    private final FlywheelSim shooter;

    private final AnglePIDController hoodController;
    private final PIDController shooterController;

    public HoodedShooterSim() {
        super();
        
        hood = new LinearSystemSim<N2, N1, N2>(LinearSystemId.identifyPositionSystem(
            3.0,
            0.1
        ));

        shooter = new FlywheelSim(
            LinearSystemId.createFlywheelSystem(
                DCMotor.getKrakenX44(2),
                1.0,
                3.0
            ),
            DCMotor.getKrakenX44(2)
        );
        
        hoodController = new AnglePIDController(
            Gains.HoodedShooter.Hood.kP,
            Gains.HoodedShooter.Hood.kI,
            Gains.HoodedShooter.Hood.kD
        );

        shooterController = new PIDController(
            Gains.HoodedShooter.Shooter.kD,
            Gains.HoodedShooter.Shooter.kD,
            Gains.HoodedShooter.Shooter.kD
        );
    }

    public Rotation2d getCurrentAngle() {
        return Rotation2d.fromRotations(hood.getOutput(0));
    }

    @Override
    public void periodic() {
        super.periodic();

        hoodController.update(Angle.fromRotation2d(getTargetAngle()), Angle.fromRotation2d(getCurrentAngle()));
        shooterController.update(getTargetRPM(), getCurrentRPS() * 60);
        // SmartDashboard.putNumber("hdsr/Output Voltage", controller);

        hood.setInput(hoodController.getOutput());
        shooter.setInputVoltage(shooterController.getOutput());
    }

    @Override
    public void simulationPeriodic(){
        super.simulationPeriodic();

        hood.update(Settings.DT);
        shooter.update(Settings.DT);      
    }

	@Override
	public double getCurrentRPS() {
        return shooter.getOutput(0);
	}
}
