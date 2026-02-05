package com.stuypulse.robot.commands;

import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter;
import com.stuypulse.robot.subsystems.hoodedshooter.HoodedShooter.HoodedShooterState;
import com.stuypulse.robot.subsystems.swerve.CommandSwerveDrivetrain;
import com.stuypulse.robot.util.ShotCalculator.AlignAngleSolution;
import com.stuypulse.stuylib.math.Vector2D;
import com.stuypulse.robot.util.ShotCalculator;
import com.stuypulse.robot.Robot;
import com.stuypulse.robot.constants.Constants;
import com.stuypulse.robot.constants.Field;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.FieldObject2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command; 

public class TurretHoodAlignToTarget extends Command{
    private final HoodedShooter hdsr;
    private final CommandSwerveDrivetrain swerve;
    // private final Turret turret;

    private final Field2d field;

    private Pose3d targetPose;

    private FieldObject2d targetPose2d;
    private FieldObject2d virtualHubPose2d;

    private ChassisSpeeds prevfieldRelRobotSpeeds;
    private ChassisSpeeds fieldRelRobotSpeeds;

    
    public TurretHoodAlignToTarget() {
        hdsr = HoodedShooter.getInstance();
        swerve = CommandSwerveDrivetrain.getInstance();

        field = Field.FIELD2D;
        virtualHubPose2d = field.getObject("virtualHubPose");
        targetPose2d = field.getObject("targetPose");

        prevfieldRelRobotSpeeds = new ChassisSpeeds();
        fieldRelRobotSpeeds = new ChassisSpeeds();

        addRequirements(hdsr);
    }
     
    @Override
    public void initialize() {
   
    }

    @Override
    public void execute() {
        

        // update targetPose each tick
        if (hdsr.getState() == HoodedShooterState.SHOOT) {
            targetPose = Field.hubCenter3d;
        }
        else {
            targetPose = Field.hubCenter3d; // placeholder
        }

        Pose2d currentPose = swerve.getPose();

        prevfieldRelRobotSpeeds = fieldRelRobotSpeeds;

        Vector2D swerveFieldRelativeSpeeds = swerve.getFieldRelativeSpeeds(); // why is this vector2d >:(
        fieldRelRobotSpeeds = new ChassisSpeeds(swerveFieldRelativeSpeeds.x, swerveFieldRelativeSpeeds.y, swerveFieldRelativeSpeeds.getAngle().toRadians());
        
        AlignAngleSolution sol = ShotCalculator.solveShootOnTheFly(
            new Pose3d(currentPose), // TODO: add the field relative shooter offset on the robot
            targetPose,
            prevfieldRelRobotSpeeds,
            fieldRelRobotSpeeds,
            hdsr.getCurrentRPS(), 
            Constants.Align.MAX_ITERATIONS,
            Constants.Align.TIME_TOLERANCE
        );

        hdsr.setTargetAngle(sol.launchPitchAngle());
        
        // this is the required yaw for shooting into the effective hub
        Rotation2d targetTurretAngle = sol.requiredYaw().minus(currentPose.getRotation()) ;

        targetPose2d.setPose(targetPose.toPose2d());
        virtualHubPose2d.setPose(sol.estimateTargetPose().toPose2d());
  
        SmartDashboard.putNumber("hdsr/calculated yaw", targetTurretAngle.getDegrees());
        // TODO: set turret angle here    
                         
    }

    
}