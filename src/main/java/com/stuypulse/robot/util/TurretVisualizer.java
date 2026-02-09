package com.stuypulse.robot.util;



import com.stuypulse.robot.Robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

// make mechanism 2d 
//make width and height attributes for canvas.
//Units.inchesToMeters
//Make method that takes in the angle and the boolean (change color based on boolean)
public class TurretVisualizer {
    private static final TurretVisualizer instance;

    static {
        instance = new TurretVisualizer();
    }

    public static TurretVisualizer getInstance() {
        return instance;
    }

    private final Mechanism2d turretCanvas;
    private final MechanismRoot2d turretRoot;
    private final MechanismLigament2d actualTurretRepresentation;

    private final double width; //canvas size shouldn't be changing
    private final double length;

    public TurretVisualizer() {
        width = Units.inchesToMeters(20);
        length = Units.inchesToMeters(20);
        
        turretCanvas = new Mechanism2d(width, length);

        turretRoot = turretCanvas.getRoot("Turret Root", width / 2.0, length / 2.0); 
        actualTurretRepresentation = turretRoot.append(
            new MechanismLigament2d("Actual Turret Angle Representation",
             Units.inchesToMeters(5),
             Robot.isBlue() ? 0.0 : 180.0,
             Units.inchesToMeters(4), 
             new Color8Bit(Color.kBlue))); 
        //learned that we start at the angle that the robot should be starting at, hence we check our alliance color
    }

    public void updateTurretAngle(Rotation2d turretAngle, boolean atTargetAngle) {
        SmartDashboard.putData("Turret Visualizer", turretCanvas);

        actualTurretRepresentation.setAngle(turretAngle);
        actualTurretRepresentation.setColor((atTargetAngle) ? new Color8Bit(Color.kGreen) : new Color8Bit(Color.kRed));
        //I don't think you need to reset position of the root each time as it is not moving and is initialized in the constructor
    } 
}
