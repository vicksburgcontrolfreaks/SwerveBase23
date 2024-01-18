// package frc.robot.autos;

// import com.pathplanner.lib.PathConstraints;
// import com.pathplanner.lib.PathPlanner;
// import com.pathplanner.lib.PathPlannerTrajectory;
// import com.pathplanner.lib.PathPoint;
// import com.pathplanner.lib.commands.PPSwerveControllerCommand;
// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.math.controller.ProfiledPIDController;
// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.math.geometry.Rotation2d;
// import edu.wpi.first.math.geometry.Translation2d;
// import edu.wpi.first.math.util.Units;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;
// import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
// import frc.robot.Constants;
// import frc.robot.Constants.AutoConstants;
// import frc.robot.RobotContainer;
// import frc.robot.commands.SelfBalanceCommand;
// import frc.robot.subsystems.ElevatorSubsystem;
// import frc.robot.subsystems.IntakeSubsystem;
// import frc.robot.subsystems.WristSubsystem;

// public class AutoPreloadConeChargeStationPlusCone extends SequentialCommandGroup {
//   public AutoPreloadConeChargeStationPlusCone(RobotContainer robot) {

//     PathPlannerTrajectory trajectory1 =
//         PathPlanner.generatePath(
//             new PathConstraints(1.5, AutoConstants.kMaxAccelerationMetersPerSecondSquared),
//             new PathPoint(
//                 new Translation2d(Units.inchesToMeters(0), Units.inchesToMeters(0)),
//                 Rotation2d.fromDegrees(0),
//                 Rotation2d.fromDegrees(0)),
//             new PathPoint(
//                 new Translation2d(Units.inchesToMeters(74), Units.inchesToMeters(0)), // 178
//                 Rotation2d.fromDegrees(0),
//                 Rotation2d.fromDegrees(0)),
//             new PathPoint(
//                 new Translation2d(Units.inchesToMeters(200), Units.inchesToMeters(0)),
//                 Rotation2d.fromDegrees(0),
//                 Rotation2d.fromDegrees(180)));
//     PathPlannerTrajectory trajectory2 =
//         PathPlanner.generatePath(
//             new PathConstraints(1.5, AutoConstants.kMaxAccelerationMetersPerSecondSquared),
//             new PathPoint(
//                 new Translation2d(Units.inchesToMeters(200), Units.inchesToMeters(0)),
//                 Rotation2d.fromDegrees(180),
//                 Rotation2d.fromDegrees(180)),
//             new PathPoint(
//                 new Translation2d(Units.inchesToMeters(90), Units.inchesToMeters(0)),
//                 Rotation2d.fromDegrees(180),
//                 Rotation2d.fromDegrees(180)));

//     var thetaController =
//         new ProfiledPIDController(
//             Constants.AutoConstants.kPThetaController,
//             0,
//             0,
//             Constants.AutoConstants.kThetaControllerConstraints);
//     thetaController.enableContinuousInput(0, 2 * Math.PI);

//     PPSwerveControllerCommand swerveControllerCommand1 =
//         new PPSwerveControllerCommand(
//             trajectory1,
//             robot.s_Swerve::getPose,
//             Constants.Swerve.swerveKinematics,
//             new PIDController(Constants.AutoConstants.kPXController, 0, 0),
//             new PIDController(Constants.AutoConstants.kPYController, 0, 0),
//             new PIDController(Constants.AutoConstants.kPThetaController, 0, 0),
//             robot.s_Swerve::setModuleStates,
//             robot.s_Swerve);
//     PPSwerveControllerCommand swerveControllerCommand2 =
//         new PPSwerveControllerCommand(
//             trajectory2,
//             robot.s_Swerve::getPose,
//             Constants.Swerve.swerveKinematics,
//             new PIDController(Constants.AutoConstants.kPXController, 0, 0),
//             new PIDController(Constants.AutoConstants.kPYController, 0, 0),
//             new PIDController(Constants.AutoConstants.kPThetaController, 0, 0),
//             robot.s_Swerve::setModuleStates,
//             robot.s_Swerve);

//     addCommands(
//         new InstantCommand(
//             () ->
//                 robot.s_Swerve.resetOdometry(
//                     new Pose2d(new Translation2d(0, 0), Rotation2d.fromDegrees(0)))),

//         // Set LEDs to yellow
//         new InstantCommand(
//             () -> robot.s_Intake.setGamePiece(IntakeSubsystem.GamePiece.Cone), robot.s_Intake),

//         // Raise elevator and tilt wrist slightly out
//         new InstantCommand(
//             () -> robot.s_wrist.goToAngle(WristSubsystem.Positions.SLIGHTLY_OUT), robot.s_wrist),
//         new InstantCommand(
//             () -> robot.s_elevator.goToPosition(ElevatorSubsystem.Positions.HIGH),
//             robot.s_elevator),
//         new WaitUntilCommand(robot.s_elevator::isElevatorAtGoal),

//         // Lower wrist
//         new InstantCommand(
//             () -> robot.s_wrist.goToAngle(WristSubsystem.Positions.INTAKE), robot.s_wrist),
//         new WaitUntilCommand(robot.s_wrist::isWristAtGoal),

//         // Spit out game piece
//         new InstantCommand(
//             () -> robot.s_Intake.outtakeGamePiece(IntakeSubsystem.GamePiece.Cone), robot.s_Intake),
//         new WaitCommand(0.5),
//         new InstantCommand(() -> robot.s_Intake.runIntake(0), robot.s_Intake),

//         // Lower elevator and raise wrist
//         new InstantCommand(
//             () -> robot.s_wrist.goToAngle(WristSubsystem.Positions.VERTICAL), robot.s_wrist),
//         new InstantCommand(
//             () -> robot.s_elevator.goToPosition(ElevatorSubsystem.Positions.FLOOR),
//             robot.s_elevator),
//         new WaitUntilCommand(robot.s_elevator::isElevatorAtGoal),

//         // Drive onto charge station
//         new ParallelCommandGroup(
//             swerveControllerCommand1,
//             new SequentialCommandGroup(
//                 new WaitCommand(1.4),
//                 // Lower wrist
//                 new InstantCommand(
//                     () -> robot.s_wrist.goToAngle(WristSubsystem.Positions.INTAKE), robot.s_wrist),
//                 // Drive into cone and pick it up
//                 new InstantCommand(
//                     () -> robot.s_Intake.intakeGamePiece(IntakeSubsystem.GamePiece.Cone),
//                     robot.s_Intake))),

//         // Raise wrist
//         new InstantCommand(
//             () -> robot.s_wrist.goToAngle(WristSubsystem.Positions.VERTICAL), robot.s_wrist),

//         // Drive back on charge station
//         swerveControllerCommand2,

//         // Stop intake
//         new InstantCommand(() -> robot.s_Intake.runIntake(0), robot.s_Intake),

//         // Balance
//         new ParallelRaceGroup(new SelfBalanceCommand(robot.s_Swerve), new WaitCommand(3)),

//         // Stop
//         new InstantCommand(() -> robot.s_Swerve.setX(), robot.s_Swerve),
//         new WaitCommand(3));
//     // new InstantCommand(() -> robot.s_Swerve.drive(new Translation2d(0, 0), 0, false, true)));
//   }
// }
