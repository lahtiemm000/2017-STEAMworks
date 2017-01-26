package org.usfirst.frc.team4915.steamworks;


import org.usfirst.frc.team4915.steamworks.commands.IntakeSetCommand;
import org.usfirst.frc.team4915.steamworks.commands.IntakeEncoderUpdateCommand;
import org.usfirst.frc.team4915.steamworks.commands.DriveTicksCommand;
import org.usfirst.frc.team4915.steamworks.subsystems.Intake.State;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.io.IOException;

public class OI
{

    // Ports for joysticks
    public static final int DRIVE_STICK_PORT = 0;
    public static final int AUX_STICK_PORT = 1;

    public final Joystick m_driveStick = new Joystick(DRIVE_STICK_PORT);
    public final Joystick m_auxStick = new Joystick(AUX_STICK_PORT);

    public final JoystickButton m_ticksOn = new JoystickButton(m_auxStick, 3);
    public final JoystickButton m_intakeOn = new JoystickButton(m_driveStick, 7);
    public final JoystickButton m_intakeOff = new JoystickButton(m_driveStick, 9);
    public final JoystickButton m_intakeReverse = new JoystickButton(m_driveStick, 11);
    public final JoystickButton m_intakeCount = new JoystickButton(m_driveStick, 5);

    private Robot m_robot;
    private SendableChooser<Command> m_chooser;
    private Logger m_logger;

    public OI(Robot robot)
    {
        m_robot = robot;
        m_logger = new Logger("OI", Logger.Level.DEBUG);
        initAutoOI();
        initDrivetrainOI();
        initIntakeOI();
        initLauncherOI();
        initClimberOI();

        /* VERSION STRING!! */
        try (InputStream manifest = getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF"))
        {
            // build a version string
            Attributes attributes = new Manifest(manifest).getMainAttributes();
            String buildStr = "by: " + attributes.getValue("Built-By") +
                    "  on: " + attributes.getValue("Built-At") +
                    "  vers:" + attributes.getValue("Code-Version");
            SmartDashboard.putString("Build", buildStr);
            m_logger.notice("Build " + buildStr);
            ;
        }
        catch (IOException e)
        {
            SmartDashboard.putString("Build", "version not found!");
            m_logger.error("Build version not found!");
            m_logger.exception(e, true /* no stack trace needed */);
        }
    }

    private void initAutoOI()
    {
        m_chooser = new SendableChooser<>();
        m_chooser.addDefault("Default Auto", 
                             new IntakeSetCommand(m_robot.getIntake(), State.OFF));
        // chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", m_chooser);
    }

    public Command getAutoCommand()
    {
        return m_chooser.getSelected();
    }

    private void initDrivetrainOI()
    {
         m_robot.getDrivetrain().setDriveStick(m_driveStick);
         m_ticksOn.whenActive(new DriveTicksCommand(m_robot.getDrivetrain()));
    }

    private void initIntakeOI()
    {
        if (m_robot.getIntake().initialized())
        {
            m_intakeOn.whenPressed(new IntakeSetCommand(m_robot.getIntake(), State.ON));
            m_intakeOff.whenPressed(new IntakeSetCommand(m_robot.getIntake(), State.OFF));
            m_intakeReverse.whenPressed(new IntakeSetCommand(m_robot.getIntake(), State.REVERSE));
            m_intakeCount.whenPressed(new IntakeEncoderUpdateCommand(m_robot.getIntake()));
        }
    }

    private void initLauncherOI()
    {
        // includes carousel
    }

    private void initClimberOI()
    {
    }
}
