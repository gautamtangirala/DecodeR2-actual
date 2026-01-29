package org.firstinspires.ftc.teamcode.subsystems.indivdual;


import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.carouselSlots;

import android.content.ActivityNotFoundException;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.AxonEncoder.AbsoluteAnalogEncoder;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.ServoGroup;
import dev.nextftc.hardware.positionable.SetPosition;

/*
New spindexer class.
2 set position axon servos geared 2:1 to the carousel

0 -> 1 linear maps to 0 -> 355 degrees or 0 -> 710 degrees of spindexer rotation due to gearing.

If GPP: Green to 2
If PGP: Green to 1
If PPG: Green to 3
 */
public class posSpindexer implements Subsystem {
    public static final posSpindexer INSTANCE = new posSpindexer();
    private posSpindexer() {}
    private AnalogInput spindexerAnalog;
    private double actualServoAngle;
    private ServoEx rightSpindexerServo = new ServoEx("rightSpindexerServo");
    private ServoEx leftSpindexerServo = new ServoEx("leftSpindexerServo");

    private ServoGroup spindexerServos = new ServoGroup(rightSpindexerServo, leftSpindexerServo);
    private final double step = 0.19;

    private final double ID0IntakePos = 0.06;
    public final Command ID0Intake = new SetPosition(spindexerServos, ID0IntakePos).requires(spindexerServos);
    public final Command ID1LeftBack = new SetPosition(spindexerServos, ID0IntakePos).requires(spindexerServos);
    public final Command ID2RightBack = new SetPosition(spindexerServos, ID0IntakePos).requires(spindexerServos);

    private final double ID1IntakePos = ID0IntakePos + step;
    public final Command ID1Intake = new SetPosition(spindexerServos, ID1IntakePos).requires(spindexerServos);
    public final Command ID2LeftBack = new SetPosition(spindexerServos, ID1IntakePos).requires(spindexerServos);
    public final Command ID0RightBack = new SetPosition(spindexerServos, ID1IntakePos).requires(spindexerServos);

    private final double ID2IntakePos = ID1IntakePos + step;
    public final Command ID2Intake = new SetPosition(spindexerServos, ID2IntakePos).requires(spindexerServos);
    public final Command ID0LeftBack = new SetPosition(spindexerServos, ID2IntakePos).requires(spindexerServos);
    public final Command ID1RightBack = new SetPosition(spindexerServos, ID2IntakePos).requires(spindexerServos);

    private final double ShootAllPos = 1;
    public final Command shootAll = new SetPosition(spindexerServos, ShootAllPos).requires(spindexerServos);

    public final Command setSpindexerPos(double pos){
        return new SetPosition(spindexerServos, pos).requires(spindexerServos);
    }

    public final Command nextSlot(){
        double pos = spindexerServos.getPosition();
        double nextPos = pos == ID0IntakePos ? ID1IntakePos : pos == ID1IntakePos ? ID2IntakePos : ID0IntakePos;

        return new SetPosition(spindexerServos, nextPos).requires(spindexerServos);
    }

    @Override
    public void initialize(){
        spindexerAnalog = ActiveOpMode.hardwareMap().get(AnalogInput.class, "spindexerEncoder");
        ID0Intake.schedule();
    }


    @Override
    public void periodic(){

//        ActiveOpMode.telemetry().addLine("Spindexer");
//        ActiveOpMode.telemetry().addData("spindexer position: ", spindexerServos.getPosition());
//        ActiveOpMode.telemetry().addLine();
    }
}
