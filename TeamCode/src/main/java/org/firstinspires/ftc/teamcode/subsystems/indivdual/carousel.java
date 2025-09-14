package org.firstinspires.ftc.teamcode.subsystems.indivdual;


import com.qualcomm.robotcore.hardware.Servo;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class carousel implements Subsystem {
    public static final carousel INSTANCE = new carousel();
    private carousel() {}
    private ServoEx rotationServo = new ServoEx("CarouselServo");

    private final double ID1IntakePos = 0.1;
    public final Command ID1Intake = new SetPosition(rotationServo, ID1IntakePos).requires(this);
    public final Command ID2HumanPlayer = ID1Intake;
    public final Command ID3Flywheel = ID1Intake;

    private final double ID2IntakePos = 0.1;
    public final Command ID2Intake = new SetPosition(rotationServo, ID2IntakePos).requires(this);
    public final Command ID3HumanPlayer = ID2Intake;
    public final Command ID1Flywheel = ID2Intake;

    private final double ID3IntakePos = 0.1;
    public final Command ID3Intake = new SetPosition(rotationServo, ID3IntakePos).requires(this);
    public final Command ID1HumanPlayer = ID3Intake;
    public final Command ID2Flywheel = ID3Intake;


}
