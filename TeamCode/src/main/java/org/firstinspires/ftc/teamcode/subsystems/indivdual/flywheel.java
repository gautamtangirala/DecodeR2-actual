package org.firstinspires.ftc.teamcode.subsystems.indivdual;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class flywheel implements Subsystem {
    public static final flywheel INSTANCE = new flywheel();
    private flywheel() {}

    //flywheel gate
    private ServoEx gateServo = new ServoEx("FlywheelGateServo");

    private final double gateOpenPos = 0.1;
    public final Command openGate = new SetPosition(gateServo, gateOpenPos).requires(this);

    private final double gateClosePos = 0.1;
    public final Command closeGate = new SetPosition(gateServo, gateClosePos).requires(this);



    //flywheel
    private MotorGroup motorGroup = new MotorGroup(
            new MotorEx("FlywheelMotor"),
            new MotorEx("FlywheelMotor2").reversed()
    );

    private ControlSystem controlSystem = ControlSystem.builder()
            .posPid(0.005, 0, 0)
            .build();


    public final Command shootBall(double x, double y, double angle){
        double ballVelocity = 0;
        return new RunToVelocity(controlSystem, ballVelocity, -1).requires(this);
    }

    public final Command stop = new RunToVelocity(controlSystem, 0, -1).requires(this);


    //kicker
    private ServoEx kickServo = new ServoEx("FlywheelKickServo");

    private final double kickBallInPos = 0.1;
    public final Command kickBallIn = new SetPosition(kickServo, kickBallInPos).requires(this);

    private final double resetKickerPos = 0.1;
    public final Command resetKicker = new SetPosition(kickServo, resetKickerPos).requires(this);




}
