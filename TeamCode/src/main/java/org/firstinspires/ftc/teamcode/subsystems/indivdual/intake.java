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
    //gate
    private ServoEx gateServo = new ServoEx("IntakeGateServo");

    private final double gateOpenPos = 0.1;
    public final Command openGate = new SetPosition(gateServo, gateOpenPos).requires(this);

    private final double gateClosePos = 0.1;
    public final Command closeGate = new SetPosition(gateServo, gateClosePos).requires(this);


    //intake
    private MotorGroup intakeMotors = new MotorGroup(
            new MotorEx("intakeMotorLeft"),
            new MotorEx("intakeMotorRight").reversed()
    );

    public final Command in = new SetPower(intakeMotors, 1).requires(this);
    public final Command out = new SetPower(intakeMotors, -1).requires(this);
    public final Command stop = new SetPower(intakeMotors, 0).requires(this);
}
