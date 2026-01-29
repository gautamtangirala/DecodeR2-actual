package org.firstinspires.ftc.teamcode.subsystems.indivdual;


import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.SwitchCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.ServoGroup;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.positionable.SetPositions;

public class kicker implements Subsystem {
    public static final kicker INSTANCE = new kicker();
    private kicker() {}
    //kicker
    private ServoEx kickerServo = new ServoEx("kicker");

    private final double rampUpPos = 0.76
            ;
    private final double rampDownPos = 0.66;
    public final Command kickerUp = new SetPosition(kickerServo, rampUpPos);
    public final Command kickerDown = new SetPosition(kickerServo, rampDownPos);
    public final Command setKickerPos(double pos){
        return new SetPosition(kickerServo, pos).requires();
    }


    @Override
    public void initialize(){
        kickerDown.schedule();
    }

    @Override
    public void periodic(){

//        ActiveOpMode.telemetry().addLine("Kicker");
//        ActiveOpMode.telemetry().addData("kicker position", kickerServo.getPosition());
//        ActiveOpMode.telemetry().addLine();
    }
}
