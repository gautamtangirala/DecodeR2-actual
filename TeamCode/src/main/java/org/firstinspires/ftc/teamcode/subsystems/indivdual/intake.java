package org.firstinspires.ftc.teamcode.subsystems.indivdual;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

public class intake implements Subsystem {
    public static final intake INSTANCE = new intake();
    private intake() { }


    //intake
    private MotorEx intakeMotor = new MotorEx("intake");

    public final Command in = new SetPower(intakeMotor, 1).requires(intakeMotor);
    public final Command out = new SetPower(intakeMotor, -1).requires(intakeMotor);
    public final Command stop = new SetPower(intakeMotor, 0).requires(intakeMotor);
}
