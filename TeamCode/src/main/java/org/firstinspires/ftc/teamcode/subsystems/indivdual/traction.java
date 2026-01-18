package org.firstinspires.ftc.teamcode.subsystems.indivdual;


import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.SwitchCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.ServoGroup;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.positionable.SetPositions;

public class traction implements Subsystem {
    public static final traction INSTANCE = new traction();
    private traction() {}
    private ServoEx rightTractionWheel = new ServoEx("rightTractionWheel");
    private ServoEx leftTractionWheel = new ServoEx("leftTractionWheel");

    private double rightTractionUp = 0.61;
    private double rightTractionDown = 0.64;
    private double rightTractionTip = 0.71;

    private double leftTractionUp = 0.96;
    private double leftTractionDown = 1;
    private double leftTractionTip = 1;

    public final Command deploy = new SetPositions(rightTractionWheel.to(rightTractionDown), leftTractionWheel.to(leftTractionDown)).requires(this);

    public final Command tip = new SetPositions(rightTractionWheel.to(rightTractionTip), leftTractionWheel.to(leftTractionTip)).requires(this);

    public final Command retract = new SetPositions(rightTractionWheel.to(rightTractionUp), leftTractionWheel.to(leftTractionUp)).requires(this);


    public final Command setRightPos(double pos){
        return new SetPosition(rightTractionWheel, pos).requires();
    }

    public final Command setLeftPos(double pos){
        return new SetPosition(leftTractionWheel, pos).requires();
    }

    @Override
    public void initialize(){
        traction.INSTANCE.retract.invoke();
    }

    @Override
    public void periodic(){
    }
}
