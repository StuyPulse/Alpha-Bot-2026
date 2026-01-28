package com.stuypulse.robot.subsystems.turret;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Motors;
import com.stuypulse.robot.constants.Ports;
import com.stuypulse.robot.constants.Settings;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.util.SysId;
import com.stuypulse.robot.util.vision.HubUtil;
import com.stuypulse.robot.util.vision.HubUtil.FerryTargetPositions;
import com.stuypulse.stuylib.math.Vector2D;

import java.util.Optional;

import com.ctre.phoenix6.controls.MotionMagicVoltage;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class TurretImpl extends Turret {
    private final TalonFX motor;
    private final CANcoder encoder17t;
    private final CANcoder encoder18t;
    private boolean hasUsedAbsoluteEncoder;
    private FerryTargetPositions targetPosition;
    private Optional<Double> voltageOverride;

    public TurretImpl() {
        motor = new TalonFX(Ports.Turret.MOTOR, Ports.bus);
        encoder17t = new CANcoder(Ports.Turret.ENCODER17T, Ports.bus);
        encoder18t = new CANcoder(Ports.Turret.ENCODER18T, Ports.bus);

        Motors.Turret.turretMotor.configure(motor);
        encoder17t.getConfigurator().apply(Motors.Turret.turretEncoder17t);
        encoder18t.getConfigurator().apply(Motors.Turret.turretEncoder18t);

        hasUsedAbsoluteEncoder = false;
        voltageOverride = Optional.empty();
        targetPosition = FerryTargetPositions.LEFT_WALL; //TODO: VERIFY WE WANT TO DEFAULT TO THIS
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
                * (double) Constants.Turret.Encoder17t.TEETH);

        final Rotation2d encoder18tPosition = getEncoderPos18t();
        final double numberOfGearTeethRotated18 = (encoder18tPosition.getRotations()
                * (double) Constants.Turret.Encoder18t.TEETH);

        final double crt_Partial17 = numberOfGearTeethRotated17 * inverseMod17t * Constants.Turret.Encoder17t.TEETH;
        final double crt_Partial18 = numberOfGearTeethRotated18 * inverseMod18t * Constants.Turret.Encoder18t.TEETH;

        double crt_pos = (crt_Partial17 + crt_Partial18)
                % (Constants.Turret.Encoder17t.TEETH * Constants.Turret.Encoder18t.TEETH);

        // Java's % operator is not actually the same as the modulo operator, the lines below account for that 
        crt_pos = (crt_pos < 0) ? (crt_pos + Constants.Turret.Encoder17t.TEETH * Constants.Turret.Encoder18t.TEETH)
                : crt_pos;

        final double turretAngle = (crt_pos / (double) Constants.Turret.BigGear.TEETH);

        return Rotation2d.fromRotations(turretAngle);
    }

    @Override
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
    public Rotation2d getFerryAngle() {
        Vector2D robot = new Vector2D(CommandSwerveDrivetrain.getInstance().getPose().getTranslation());
        Vector2D robotToHub = robot
                .sub(new Vector2D(targetPosition.getFerryTargetPose().getTranslation())).normalize();
        Vector2D zeroVector = new Vector2D(0.0, 1.0);
        // define this as a constant somewhere?

        Rotation2d angle = Rotation2d
                .fromDegrees(Math.acos(robotToHub.dot(zeroVector) / robotToHub.magnitude() * zeroVector.magnitude()));
        return angle;
    }
    
    @Override
    public Rotation2d getTurretAngle() {
        return Rotation2d.fromRotations(motor.getPosition().getValueAsDouble());
    }

    @Override
    public boolean atTargetAngle() {
        return Math.abs(getTurretAngle().minus(getTargetAngle()).getDegrees() + 180.0) < Settings.Turret.TOLERANCE_DEG;
    }

    // SYSID STUFFS
    @Override
    public SysIdRoutine getSysIdRoutine() {
        return SysId.getRoutine(
                2,
                6,
                "Turret",
                voltage -> setVoltageOverride(Optional.of(voltage)),
                () -> this.motor.getPosition().getValueAsDouble(),
                () -> this.motor.getVelocity().getValueAsDouble(),
                () -> this.motor.getMotorVoltage().getValueAsDouble(),
                getInstance());
    }

    private void setVoltageOverride(Optional<Double> volts) {
        this.voltageOverride = volts;
    }

    @Override
    public void periodic() {
        if (!hasUsedAbsoluteEncoder || getAbsoluteTurretAngle().getRotations() > 1.0 || getTurretAngle().getRotations() < 0.0) {
            motor.setPosition((getAbsoluteTurretAngle().getDegrees() % 360.0) / 360.0);
            hasUsedAbsoluteEncoder = true;
            System.out.println("Absolute Encoder Reset triggered");
        }
        
        if (Settings.EnabledSubsystems.TURRET.get()) {
            if (voltageOverride.isPresent())
                motor.setVoltage(voltageOverride.get());
            else
                motor.setControl(new MotionMagicVoltage(getTargetAngle().getRotations()));
        } 
        else motor.setVoltage(0.0);

        SmartDashboard.putNumber("Turret/Encoder18t Abs Position (Rot)", encoder18t.getAbsolutePosition().getValueAsDouble());
        SmartDashboard.putNumber("Turret/Encoder17t Abs Position (Rot)", encoder17t.getAbsolutePosition().getValueAsDouble());
        SmartDashboard.putNumber("Turret/Position (Rot)", getAbsoluteTurretAngle().getRotations());
    }
}
