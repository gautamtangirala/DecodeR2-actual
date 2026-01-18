package org.firstinspires.ftc.teamcode.subsystems.indivdual;


import static org.firstinspires.ftc.teamcode.opmode.tuners.carouselTuner.spindexerD;
import static org.firstinspires.ftc.teamcode.opmode.tuners.carouselTuner.spindexerP;
import static org.firstinspires.ftc.teamcode.opmode.tuners.carouselTuner.target;
import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.carouselSlots;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.AxonEncoder.AbsoluteAnalogEncoder;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.AxonEncoder.Angle;

import java.util.ResourceBundle;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.impl.CRServoEx;
import dev.nextftc.hardware.impl.FeedbackCRServoEx;


public class spindexer implements Subsystem {
    public static final spindexer INSTANCE = new spindexer();
    private spindexer() {}
    private AnalogInput spindexerAnalog;
    private CRServoEx rightSpindexerServo = new CRServoEx("rightSpindexerServo");
    private CRServoEx leftSpindexerServo = new CRServoEx("leftSpindexerServo");
    public AbsoluteAnalogEncoder spindexerEncoder;

    private double lastAngle = 0;
    public static double continuousAngle = 0;
    public static double targetAngle = 0;

    private ControlSystem spindexerControl = ControlSystem.builder().posPid(spindexerP,0,spindexerD).build();


    //Servo angles needed
    private static double radToNextSlot = Math.toRadians(60);
    private static double radToBetweenSlots = Math.toRadians(30);
    private static double radToShootAll = Math.toRadians(360);
    private static long halfSlotIndex = Math.round(targetAngle / radToBetweenSlots);
    private static boolean isBetweenSlots = halfSlotIndex % 2 == 1;

    public static final void shootAll(){
        targetAngle += radToShootAll;
        carouselSlots[0] = sensors.ballColor.EMPTY;
        carouselSlots[1] = sensors.ballColor.EMPTY;
        carouselSlots[2] = sensors.ballColor.EMPTY;
    }


    public static final void nextSlot(){
            sensors.ballColor temp = carouselSlots[2];
            carouselSlots[2] = carouselSlots[1];
            carouselSlots[1] = carouselSlots[0];
            carouselSlots[0] = temp;
;            if (isBetweenSlots) {
                targetAngle += radToBetweenSlots;
            }
            else{
                targetAngle += radToNextSlot;
            }
    }

    public static final void betweenSlots() {
        if (isBetweenSlots) {
            targetAngle += 0;
        } else {
            targetAngle += radToBetweenSlots;
        }
    }

    public static boolean spindexerAtTarget(){
        return Math.abs(spindexer.targetAngle - spindexer.continuousAngle) < Math.toRadians(10);
    }


    @Override
    public void initialize(){
        spindexerAnalog = ActiveOpMode.hardwareMap().get(AnalogInput.class, "spindexerEncoder");
        spindexerEncoder = new AbsoluteAnalogEncoder(spindexerAnalog).setInverted(true).zero(-0.2);

    }

    private final ElapsedTime timer = new ElapsedTime();
    @Override
    public void periodic(){
        halfSlotIndex = Math.round(targetAngle / radToBetweenSlots);
        isBetweenSlots = halfSlotIndex % 2 == 1;


        double current = spindexerEncoder.getCurrentPosition();
        double delta = Angle.normDelta(current - lastAngle);
        double vel = delta/timer.seconds();

        continuousAngle += delta;

        lastAngle = current;
        timer.reset();

        double error = Math.abs(targetAngle - continuousAngle);
        spindexerControl.setGoal(new KineticState(targetAngle));
        double power = spindexerControl.calculate(new KineticState(continuousAngle, vel));
        rightSpindexerServo.setPower(power);
        leftSpindexerServo.setPower(power);


        ActiveOpMode.telemetry().addData("Spindexer Power", power);
        ActiveOpMode.telemetry().addData("Spindexer Target", targetAngle);
        ActiveOpMode.telemetry().addData("Spindexer Error",  error);
        ActiveOpMode.telemetry().addData("Spindexer Total Angle", continuousAngle);
        ActiveOpMode.telemetry().addData("Spindexer Current Angle", current);
        ActiveOpMode.telemetry().addData("Spindexer PID", spindexerControl.toString());

    }
}
