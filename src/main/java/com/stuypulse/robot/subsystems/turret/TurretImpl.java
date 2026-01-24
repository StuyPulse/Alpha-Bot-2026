package com.stuypulse.robot.subsystems.turret;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.util.vision.HubUtil;
import com.stuypulse.stuylib.math.Vector2D;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurretImpl extends Turret {
    private final TalonFX motor;
    private final CANcoder encoder17t;
    private final CANcoder encoder18t;

    public TurretImpl() {
        motor = new TalonFX(Ports.Turret.MOTOR, Ports.bus);
        encoder17t = new CANcoder(Ports.Turret.ENCODER17T, Ports.bus);
        encoder18t = new CANcoder(Ports.Turret.ENCODER18T, Ports.bus);

        Motors.Turret.turretMotor.configure(motor);
        encoder17t.getConfigurator().apply(Motors.Turret.turretEncoder17t);
        encoder18t.getConfigurator().apply(Motors.Turret.turretEncoder18t);
    }

    private Rotation2d getEncoderPos17t() {
        return Rotation2d.fromRotations(this.encoder17t.getAbsolutePosition().getValueAsDouble());
    }

    private Rotation2d getEncoderPos18t() {
        return Rotation2d.fromRotations(this.encoder18t.getAbsolutePosition().getValueAsDouble());
    }

    public Rotation2d getAbsoluteTurretAngle() {
        final int inverseMod17t = 1;
        final int inverseMod18t = -1;

        final Rotation2d encoder17tPosition = getEncoderPos17t();
        final double numberOfGearTeethRotated17 = (encoder17tPosition.getRotations()
                * (double) Constants.Turret.Encoder17t.teeth);

        final Rotation2d encoder18tPosition = getEncoderPos18t();
        final double numberOfGearTeethRotated18 = (encoder18tPosition.getRotations()
                * (double) Constants.Turret.Encoder18t.teeth);

        final double crt_Partial17 = numberOfGearTeethRotated17 * inverseMod17t * Constants.Turret.Encoder17t.teeth;
        final double crt_Partial18 = numberOfGearTeethRotated18 * inverseMod18t * Constants.Turret.Encoder18t.teeth;

        double crt_pos = (crt_Partial17 + crt_Partial18)
        % (Constants.Turret.Encoder17t.teeth * Constants.Turret.Encoder18t.teeth);
        
        // Java's % operator is not actually the same as the modulo operator
        crt_pos = (crt_pos < 0) ? (crt_pos + Constants.Turret.Encoder17t.teeth * Constants.Turret.Encoder18t.teeth)
                : crt_pos;

        final double turretAngle = (crt_pos / (double) Constants.Turret.BigGear.teeth);

        return Rotation2d.fromRotations(turretAngle);
    }
    
    public Rotation2d getPointAtHubAngle() {
        Vector2D robot = new Vector2D(CommandSwerveDrivetrain.getInstance().getPose().getTranslation());
        Vector2D hub = new Vector2D(HubUtil.getAllianceHubPose().getTranslation());
        Vector2D robotToHub = hub.sub(robot).normalize();
        Vector2D zeroVector = new Vector2D(0.0, 1.0);

        double crossProduct = zeroVector.x * robotToHub.y - zeroVector.y * robotToHub.x;
        double dotProduct = zeroVector.dot(robotToHub);

        Rotation2d targetAngle = Rotation2d.fromRadians(Math.atan2(crossProduct, dotProduct));

        return targetAngle;
    }
    
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Turrent/Encoder17t Abs Postition", encoder17t.getAbsolutePosition().getValueAsDouble());
        SmartDashboard.putNumber("Turrent/Encoder18t Abs Postition", encoder18t.getAbsolutePosition().getValueAsDouble());
    }
}