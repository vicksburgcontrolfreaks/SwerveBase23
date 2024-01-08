// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.autos.*;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.subsystems.IntakeSubsystem.GamePiece;
import frc.robot.subsystems.LimelightSubsystem;
import frc.robot.subsystems.LED;
/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  /* Controllers */
  boolean mitchell;
  private final Joystick gamepad = new Joystick(0);
  private final Joystick driver = new Joystick(1);
  private final Joystick buttonBox = new Joystick(2);

  /* Drive Controls */
  private final int translationAxis = Joystick.AxisType.kY.value;
  private final int strafeAxis = Joystick.AxisType.kX.value;
  private final int rotationAxis = Joystick.AxisType.kTwist.value;
   private double IHATEGITHUB;
  /* Driver Buttons */
  private final JoystickButton zeroGyro = new JoystickButton(driver, 5);
  private final JoystickButton robotCentric = new JoystickButton(driver, 3);

  private final JoystickButton slowSpeed = new JoystickButton(driver, 1);
  private final JoystickButton highSpeed = new JoystickButton(driver, 2);

  /* Subsystems */
  public final Swerve s_Swerve = new Swerve();
  public final LimelightSubsystem m_limelight = new LimelightSubsystem();
  public final ElevatorSubsystem s_elevator = new ElevatorSubsystem();
  public final WristSubsystem s_wrist = new WristSubsystem();
  public final LED led = new LED(); 
  //   LED LED LED LED LED LED LED LED LED LED LED LED LED 

  // public final EntireArmSubsystem s_EntireArm = new EntireArmSubsystem();

  public final IntakeSubsystem s_Intake = new IntakeSubsystem();

  /** The container for the robot. Contains subsystems, IO devices, and commands. */
  public RobotContainer() {

    CameraServer.startAutomaticCapture();

    m_limelight.turnOnDriverCam();
    m_limelight.enableLimelight(false);
    

    s_Swerve.setDefaultCommand(
        new TeleopSwerve(
            s_Swerve,
            () -> -driver.getRawAxis(translationAxis),
            () -> -driver.getRawAxis(strafeAxis),
            () -> -driver.getRawAxis(rotationAxis),
            () -> robotCentric.getAsBoolean(),
            () -> slowSpeed.getAsBoolean() || s_elevator.isElevatorHigh(),
            () -> highSpeed.getAsBoolean()));

    // No default elevator command
    // No default wrist command

    // s_LED.setDefaultCommand(new RunCommand(() -> s_LED.setBlue(), s_LED));

    // s_Intake.setDefaultCommand(new RunCommand(() -> s_Intake.runIntake(0), s_Intake));
    s_Intake.setDefaultCommand(
        new RunCommand(
            () -> s_Intake.runIntake(gamepad.getRawAxis(XboxController.Axis.kRightY.value)),
            s_Intake));

    // Configure the button bindings

    // configureButtonBindings(

    // ); Not clear on what this does 
    configureButtonBindings();

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  //Activates all Teleop commads based of button pressed 
  private void configureButtonBindings() {
    // Drivetrain controls
    zeroGyro.onTrue(new InstantCommand(() -> s_Swerve.zeroGyro()));
//Based of direction on small black jostick dpad on top of main controller turns the robot to its corresponidng robot centric angle
    new POVButton(driver, 0).whileTrue(new TurnToAngleCommand(s_Swerve, 180, 2));
    new POVButton(driver, 90).whileTrue(new TurnToAngleCommand(s_Swerve, 90, 2));
    new POVButton(driver, 180).whileTrue(new TurnToAngleCommand(s_Swerve, 0, 2));
    new POVButton(driver, 270).whileTrue(new TurnToAngleCommand(s_Swerve, -90, 2));
    new LED();
//     if (gamepad.getRawButton(1)){
//      solenoidSwitch.set(kForward);
  
//   }
//  if (gamepad.getRawButton(1) && solenoidSwitch.set(kForward) == true){
//      solenoidSwitch.set(kReverse);
//  }
    new JoystickButton(driver, 6).whileTrue(new RunCommand(() -> s_Swerve.setX(), s_Swerve));
    // new POVButton(gamepad, 270).whileTrue(new SelfBalanceCommand(s_Swerve));

    // new JoystickButton(gamepad, XboxController.Button.kA.value)
    //     .whileTrue(
    //         new InstantCommand(
    //             () -> m_limelight.setPipeline(LimelightSubsystem.Pipeline.RetroTape)));
    // new JoystickButton(gamepad, XboxController.Button.kB.value)
    //     .whileTrue(
    //         new InstantCommand(
    //             () -> m_limelight.setPipeline(LimelightSubsystem.Pipeline.AprilTags)));

    // Limelight controls
    // new JoystickButton(gamepad, 5).onTrue(new InstantCommand(() -> m_limelight.toggleDriverCam(),
    // m_limelight));

    // new JoystickButton(driver, 4)
    //     .onTrue(
    //         new SequentialCommandGroup(
    //             new InstantCommand(() -> m_limelight.enableLimelight(true), m_limelight),
    //             new StrafeToTargetCommand(s_Swerve, m_limelight, driver, 50)))
    //     .onFalse(
    //         new SequentialCommandGroup(
    //             new InstantCommand(() -> m_limelight.enableLimelight(false), m_limelight),
    //             new InstantCommand(() -> m_limelight.toggleDriverCam(), m_limelight)));

    // Elevator controls
    new POVButton(gamepad, 0)
        .whileTrue(
            new SequentialCommandGroup(
                new InstantCommand(
                    () -> s_wrist.goToAngle(WristSubsystem.Positions.SLIGHTLY_OUT), s_wrist),
                new InstantCommand(
                    () -> s_elevator.goToPosition(ElevatorSubsystem.Positions.HIGH), s_elevator)));
    new POVButton(gamepad, 90)
        .whileTrue(
            new SequentialCommandGroup(
                new InstantCommand(
                    () -> s_wrist.goToAngle(WristSubsystem.Positions.SLIGHTLY_OUT), s_wrist),
                new InstantCommand(
                    () -> s_elevator.goToPosition(ElevatorSubsystem.Positions.MID), s_elevator)));
    new POVButton(gamepad, 270)
        .whileTrue(
            new SequentialCommandGroup(
                new InstantCommand(
                    () -> s_wrist.goToAngle(WristSubsystem.Positions.SLIGHTLY_OUT), s_wrist),
                new InstantCommand(
                    () ->
                        s_elevator.goToPosition(
                            ElevatorSubsystem.Positions.SUBSTATION_SIDEWAYS_CONE),
                    s_elevator)));
    new POVButton(gamepad, 180)
        .whileTrue(
            new RunCommand(
                () -> s_elevator.goToPosition(ElevatorSubsystem.Positions.FLOOR), s_elevator));

    // Wrist controls
    new JoystickButton(gamepad, XboxController.Button.kLeftBumper.value)
        .whileTrue(
            new RunCommand(() -> s_wrist.goToAngle(WristSubsystem.Positions.VERTICAL), s_wrist));
    // new JoystickButton(gamepad, XboxController.Button.kRightBumper.value)
    //     .whileTrue(
    //         new RunCommand(() -> s_wrist.goToAngle(WristSubsystem.Positions.INTAKE), s_wrist));
    new JoystickButton(gamepad, XboxController.Button.kRightBumper.value)
        .whileTrue(
            new ConditionalCommand(
                new RunCommand(
                    () -> s_wrist.goToAngle(WristSubsystem.Positions.TOP_CONE_SCORE), s_wrist),
                new RunCommand(() -> s_wrist.goToAngle(WristSubsystem.Positions.INTAKE), s_wrist),
                () -> s_elevator.isElevatorHigh()));

    new Trigger(() -> gamepad.getRawAxis(3) > 0.5)
        .whileTrue(
            new RunCommand(
                () -> s_wrist.goToAngle(WristSubsystem.Positions.SUBSTATION_INTAKE), s_wrist));

    // LED controls
    new JoystickButton(gamepad, XboxController.Button.kX.value)
        .whileTrue(
            new ParallelCommandGroup(
                new RunCommand(() -> s_Intake.setGamePiece(GamePiece.Cube))
                // new RunCommand(() ->
                // m_limelight.setPipeline(LimelightSubsystem.Pipeline.AprilTags))
                // new RunCommand(() -> s_Intake.runIntake(0.5), s_Intake)
                ));
    new JoystickButton(gamepad, XboxController.Button.kY.value)
        .whileTrue(
            new ParallelCommandGroup(
                new RunCommand(() -> s_Intake.setGamePiece(GamePiece.Cone))
                // new RunCommand(() ->
                // m_limelight.setPipeline(LimelightSubsystem.Pipeline.RetroTape))
                // new RunCommand(() -> s_Intake.runIntake(-0.5), s_Intake)
                ));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    if (buttonBox.getRawButton(3)) {
      return new AutoPreloadConeChargeStation(this);
    } else if (buttonBox.getRawButton(4)) {
      return new AutoPreloadCubeChargeStation(this);
    } else if (buttonBox.getRawButton(5)) {
      return new AutoBlueLeftTwoHigh(this);
    } else if (buttonBox.getRawButton(6)) {
      return new AutoRedRight(this);
    } else if (buttonBox.getRawButton(7)) {
      return new AutoPreloadConeChargeStationPlusCone(this);
    } else {
      return new AutoMobility(this);
    }
  }
}
