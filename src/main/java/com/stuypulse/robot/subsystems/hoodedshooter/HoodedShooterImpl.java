// package com.stuypulse.robot.subsystems.hoodedshooter;

// import com.ctre.phoenix6.controls.Follower;
// import com.ctre.phoenix6.controls.PositionVoltage;
// import com.ctre.phoenix6.controls.VelocityVoltage;
// import com.ctre.phoenix6.hardware.CANcoder;
// import com.ctre.phoenix6.hardware.TalonFX;
// import com.ctre.phoenix6.signals.MotorAlignmentValue;
// import com.stuypulse.robot.constants.Motors;
// import com.stuypulse.robot.constants.Ports;
// import com.stuypulse.robot.constants.Settings;
// import com.stuypulse.robot.constants.Settings.EnabledSubsystems;

// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

// public class HoodedShooterImpl extends HoodedShooter {
//     private final TalonFX shooterLeader;
//     private final TalonFX shooterFollower;
//     private final TalonFX hoodMotor;
//     private final CANcoder hoodEncoder;

//     // Controllers
//     private final VelocityVoltage shooterController;
//     private final PositionVoltage hoodController;
//     private final Follower follower;
    
//     private boolean hasSeededHood;

//     public HoodedShooterImpl() {

//         shooterController = new VelocityVoltage(getTargetRPM() / 60.0);
//         hoodController = new PositionVoltage(getTargetAngle().getRotations());
//         follower = new Follower(Ports.HoodedShooter.Shooter.MOTOR_LEAD, MotorAlignmentValue.Opposed);

//         shooterLeader = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_LEAD);
//         shooterFollower = new TalonFX(Ports.HoodedShooter.Shooter.MOTOR_FOLLOW);
//         shooterFollower.setControl(follower);

//         hoodMotor = new TalonFX(Ports.HoodedShooter.Hood.MOTOR);
//         hoodEncoder = new CANcoder(Ports.HoodedShooter.Hood.THROUGHBORE_ENCODER);

//         Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterLeader);
//         Motors.HoodedShooter.Shooter.SHOOTER_CONFIG.configure(shooterFollower);
//         Motors.HoodedShooter.Hood.HOOD_CONFIG.configure(hoodMotor);
//     }

//     @Override
//     public Rotation2d getHoodAngle() {
//         return Rotation2d.fromRotations(hoodMotor.getPosition().getValueAsDouble());
//     }

//     @Override
//     public double getShooterRPM() {
//         return getLeaderRPM();
//     }

//     public double getLeaderRPM() {
//         return shooterLeader.getVelocity().getValueAsDouble() * 60.0;
//     }

//     public double getFollowerRPM() {
//         return shooterFollower.getVelocity().getValueAsDouble() * 60.0;
//     }

//     @Override 
//     public void periodic() {
//         super.periodic();

//         if (!hasSeededHood) {
//             hoodMotor.setPosition(hoodEncoder.getPosition().getValueAsDouble());
//         }

//         if (!EnabledSubsystems.HOODED_SHOOTER.get() || getState() == HoodedShooterState.STOW) {
//             shooterLeader.stopMotor();
//             hoodMotor.stopMotor();
//         } else {
//             shooterLeader.setControl(shooterController.withVelocity(getTargetRPM() / 60.0));
//             shooterFollower.setControl(follower);
//             hoodMotor.setControl(hoodController.withPosition(getTargetAngle().getRotations()));
//         }
        
//         if (Settings.DEBUG_MODE) {
//             // SHOOTER
//             SmartDashboard.putNumber("HoodedShooter/Shooter/Leader RPM", getLeaderRPM());
//             SmartDashboard.putNumber("HoodedShooter/Shooter/Follower RPM", getFollowerRPM());

//             SmartDashboard.putNumber("HoodedShooter/Shooter/Lead Voltage", shooterLeader.getMotorVoltage().getValueAsDouble());
//             SmartDashboard.putNumber("HoodedShooter/Shooter/Lead Voltage", shooterLeader.getStatorCurrent().getValueAsDouble());
//             SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", shooterFollower.getMotorVoltage().getValueAsDouble());
//             SmartDashboard.putNumber("HoodedShooter/Shooter/Follower Voltage", shooterFollower.getStatorCurrent().getValueAsDouble());

//             // HOOD
//             SmartDashboard.putNumber("HoodedShooter/Hood/Hood Voltage", hoodMotor.getMotorVoltage().getValueAsDouble());
//             SmartDashboard.putNumber("HoodedShooter/Hood/Hood Current", hoodMotor.getStatorCurrent().getValueAsDouble());
//         }
//     }
// }