package frc.robot.autos;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPoint;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants;
import frc.robot.Constants.AutoConstants;
import frc.robot.commands.TurnToAngleCommand;
import frc.robot.subsystems.Swerve;

public class testAuto extends SequentialCommandGroup {
  public testAuto(Swerve s_Swerve) {

    PathPlannerTrajectory trajectory1 =
        PathPlanner.generatePath(
            new PathConstraints(
                AutoConstants.kMaxSpeedMetersPerSecond,
                AutoConstants.kMaxAccelerationMetersPerSecondSquared),
            new PathPoint(
                new Translation2d(0, 0), Rotation2d.fromDegrees(180), Rotation2d.fromDegrees(0)),
            new PathPoint(
                new Translation2d(-1, 0), Rotation2d.fromDegrees(180), Rotation2d.fromDegrees(0)));
    PathPlannerTrajectory trajectory2 =
        PathPlanner.generatePath(
            new PathConstraints(
                AutoConstants.kMaxSpeedMetersPerSecond,
                AutoConstants.kMaxAccelerationMetersPerSecondSquared),
            new PathPoint(
                new Translation2d(-1, 0), Rotation2d.fromDegrees(180), Rotation2d.fromDegrees(180)),
            new PathPoint(
                new Translation2d(-5, 0),
                Rotation2d.fromDegrees(180),
                Rotation2d.fromDegrees(180)));
    PathPlannerTrajectory trajectory3 =
        PathPlanner.generatePath(
            new PathConstraints(
                AutoConstants.kMaxSpeedMetersPerSecond,
                AutoConstants.kMaxAccelerationMetersPerSecondSquared),
            new PathPoint(
                new Translation2d(-5, 0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(180)),
            new PathPoint(
                new Translation2d(-1, 0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(180)));
    PathPlannerTrajectory trajectory4 =
        PathPlanner.generatePath(
            new PathConstraints(
                AutoConstants.kMaxSpeedMetersPerSecond,
                AutoConstants.kMaxAccelerationMetersPerSecondSquared),
            new PathPoint(
                new Translation2d(-1, 0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(0)),
            new PathPoint(
                new Translation2d(0, 0), Rotation2d.fromDegrees(0), Rotation2d.fromDegrees(0)));

    var thetaController =
        new ProfiledPIDController(
            Constants.AutoConstants.kPThetaController,
            0,
            0,
            Constants.AutoConstants.kThetaControllerConstraints);
    thetaController.enableContinuousInput(0, 2 * Math.PI);

    PPSwerveControllerCommand swerveControllerCommand1 =
        new PPSwerveControllerCommand(
            trajectory1,
            s_Swerve::getPose,
            Constants.Swerve.swerveKinematics,
            new PIDController(Constants.AutoConstants.kPXController, 0, 0),
            new PIDController(Constants.AutoConstants.kPYController, 0, 0),
            new PIDController(Constants.AutoConstants.kPThetaController, 0, 0),
            s_Swerve::setModuleStates,
            s_Swerve);
    PPSwerveControllerCommand swerveControllerCommand2 =
        new PPSwerveControllerCommand(
            trajectory2,
            s_Swerve::getPose,
            Constants.Swerve.swerveKinematics,
            new PIDController(Constants.AutoConstants.kPXController, 0, 0),
            new PIDController(Constants.AutoConstants.kPYController, 0, 0),
            new PIDController(Constants.AutoConstants.kPThetaController, 0, 0),
            s_Swerve::setModuleStates,
            s_Swerve);
    PPSwerveControllerCommand swerveControllerCommand3 =
        new PPSwerveControllerCommand(
            trajectory3,
            s_Swerve::getPose,
            Constants.Swerve.swerveKinematics,
            new PIDController(Constants.AutoConstants.kPXController, 0, 0),
            new PIDController(Constants.AutoConstants.kPYController, 0, 0),
            new PIDController(Constants.AutoConstants.kPThetaController, 0, 0),
            s_Swerve::setModuleStates,
            s_Swerve);
    PPSwerveControllerCommand swerveControllerCommand4 =
        new PPSwerveControllerCommand(
            trajectory4,
            s_Swerve::getPose,
            Constants.Swerve.swerveKinematics,
            new PIDController(Constants.AutoConstants.kPXController, 0, 0),
            new PIDController(Constants.AutoConstants.kPYController, 0, 0),
            new PIDController(Constants.AutoConstants.kPThetaController, 0, 0),
            s_Swerve::setModuleStates,
            s_Swerve);

    addCommands(
        new InstantCommand(
            () ->
                s_Swerve.resetOdometry(
                    new Pose2d(new Translation2d(0, 0), Rotation2d.fromDegrees(0)))),
        swerveControllerCommand1,
        new TurnToAngleCommand(s_Swerve, 180, 2),
        swerveControllerCommand2,
        new InstantCommand(() -> s_Swerve.drive(new Translation2d(0, 0), 0, false, true)),
        new WaitCommand(1),
        swerveControllerCommand3,
        new TurnToAngleCommand(s_Swerve, 0, 2),
        swerveControllerCommand4,
        new InstantCommand(() -> s_Swerve.drive(new Translation2d(0, 0), 0, false, true)));
  }
}