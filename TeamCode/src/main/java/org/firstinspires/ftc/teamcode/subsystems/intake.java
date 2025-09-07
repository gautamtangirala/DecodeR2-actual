package org.firstinspires.ftc.teamcode.subsystems;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;

public class intake implements Subsystem {
    public static final intake INSTANCE = new intake();
    private intake() { }
    private MotorGroup intakeMotors = new MotorGroup(new MotorEx("intakeMotorLeft"), new MotorEx("intakeMotorRight").reversed());




}
