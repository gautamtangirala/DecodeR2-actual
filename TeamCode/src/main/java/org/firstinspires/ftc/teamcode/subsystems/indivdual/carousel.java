package org.firstinspires.ftc.teamcode.subsystems.indivdual;


import android.widget.Switch;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.internal.hardware.android.GpioPin;

import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.SwitchCommand;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class carousel implements Subsystem {
    public static final carousel INSTANCE = new carousel();
    private carousel() {}
    private ServoEx rotationServo = new ServoEx("carousel");

    private final double step = 0.29;

    private final double ID1IntakePos = 0.3;
    public final Command ID1Intake = new SetPosition(rotationServo, ID1IntakePos).requires(rotationServo);
    public final Command ID2HumanPlayer = ID1Intake;
    public final Command ID3Flywheel = ID1Intake;

    private final double ID2IntakePos = ID1IntakePos + step;
    public final Command ID2Intake = new SetPosition(rotationServo, ID2IntakePos).requires(rotationServo);
    public final Command ID3HumanPlayer = ID2Intake;
    public final Command ID1Flywheel = ID2Intake;

    private final double ID3IntakePos = ID2IntakePos + step;
    public final Command ID3Intake = new SetPosition(rotationServo, ID3IntakePos).requires(rotationServo);
    public final Command ID1HumanPlayer = ID3Intake;
    public final Command ID2Flywheel = ID3Intake;


    public final Command nextPosition() {
        return new SwitchCommand<>(() -> rotationServo.getPosition())
                .withCase(ID1IntakePos, new SetPosition(rotationServo, ID2IntakePos))
                .withCase(ID2IntakePos, new SetPosition(rotationServo, ID3IntakePos))
                .withDefault(new SetPosition(rotationServo, ID1IntakePos));
    }

    public final Command setPos(double pos){
        return new SetPosition(rotationServo, pos).requires();
    }


    @Override
    public void periodic(){
        ActiveOpMode.telemetry().addData("carousel position:", rotationServo.getPosition());
    }
}
