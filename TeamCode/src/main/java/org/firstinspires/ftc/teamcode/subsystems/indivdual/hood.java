package org.firstinspires.ftc.teamcode.subsystems.indivdual;


import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.internal.hardware.android.GpioPin;

import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.ServoGroup;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.positionable.SetPositions;

public class hood implements Subsystem {
    public static final hood INSTANCE = new hood();
    private hood() {}
    //kicker
    //Minimum hood angle 0.15
    private ServoEx hoodRight = new ServoEx("hoodRight");
    private ServoEx hoodLeft = new ServoEx("hoodLeft");

    private static final double lowClamp = 0.68;
    private static final double highClamp = 0.9;
    public static double currentCalcHoodAngle = lowClamp;
    public final double calculateHoodAngle(double goalDist){
        double calculatedHoodAngle = 0;
        return calculatedHoodAngle;
    }


    @Override
    public void initialize(){
    }

    @Override
    public void periodic(){
        currentCalcHoodAngle = Range.clip(currentCalcHoodAngle, lowClamp, highClamp);
        hoodRight.setPosition(currentCalcHoodAngle);
        hoodLeft.setPosition(1 - currentCalcHoodAngle);

        ActiveOpMode.telemetry().addLine("Hood");
        ActiveOpMode.telemetry().addData("hood position: ", hoodRight.getPosition());
        ActiveOpMode.telemetry().addData("hood calculated position: ", currentCalcHoodAngle);
        ActiveOpMode.telemetry().addLine();
    }
}
