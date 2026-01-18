package org.firstinspires.ftc.teamcode.subsystems.indivdual;


import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;

public class hood implements Subsystem {
    public static final hood INSTANCE = new hood();
    private hood() {}
    //kicker
    private ServoEx hoodServo = new ServoEx("hood");

    //Minimum hood angle 0.15
    private final double farHoodPos = 0.2;
    private final double closeHoodPos = 0.2;
    public final Command farPos = new SetPosition(hoodServo, farHoodPos);
    public final Command closePos = new SetPosition(hoodServo, closeHoodPos);
    public final Command setHoodPos(double pos){
        return new SetPosition(hoodServo, pos).requires();
    }


    @Override
    public void initialize(){
    }

    @Override
    public void periodic(){
    }
}
