package org.firstinspires.ftc.teamcode.subsystems.indivdual;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class sensors implements Subsystem {
    public static final sensors INSTANCE = new sensors();
    private sensors() {}

    public static RevColorSensorV3 intakeColorSensor;
    public static RevColorSensorV3 leftBackColorSensor;
    //public static RevColorSensorV3 rightBackColorSensor;
    private NormalizedRGBA intakeRGBA;
    private NormalizedRGBA leftBackRGBA;
    private NormalizedRGBA rightBackRGBA;
    private static double intakeHue;
    private static double leftBackHue;
    private static double rightBackHue;

    public enum ballColor {
        PURPLE,
        GREEN,
        EMPTY
    }


    /*
    carouselSlots[0] is Intake
    carouselSlots[1] is Left Back
    carouselSlots[2] is Right Back

    The balls will shoot on in order 2,0,1

    When rotating, left back goes to intake, right back goes to left back, and intake goes to right back

     */

    public static ballColor[] carouselSlots = {
            ballColor.EMPTY,
            ballColor.EMPTY,
            ballColor.EMPTY,
    };


    @Override
    public void initialize() {
        // initialization logic (runs on init)
        intakeColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "intakeColorSensor");
        leftBackColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "leftBackColorSensor");
    }

    @Override
    public void periodic(){
//        ActiveOpMode.telemetry().addData("Intake: ", ballColor.EMPTY);
//        ActiveOpMode.telemetry().addData("Left Back: ", ballColor.EMPTY);
//        ActiveOpMode.telemetry().addData("Right Back: ", ballColor.EMPTY);
    }

    private static ballColor hueToBallColor(double hue) {

        if (hue == 0) {
            return ballColor.EMPTY;
        }
        else if (hue < 190) {
            return ballColor.GREEN;
        }

        return ballColor.PURPLE;
    }

    private static double hasBallThreshold = 3; //CM
    private static ballColor getBallColor(RevColorSensorV3 sensor){
        if (sensor.getDistance(DistanceUnit.CM) < hasBallThreshold) {
            return hueToBallColor(JavaUtil.colorToHue(sensor.getNormalizedColors().toColor()));
        }
        return hueToBallColor(0);
    }

    public static ballColor intakeSensorReading = ballColor.EMPTY;
    public static boolean intakeBeamBroken = false;

    public static final void updateIntake(){
        intakeSensorReading = getBallColor(intakeColorSensor);
        intakeBeamBroken = intakeSensorReading != ballColor.EMPTY;
    }


    public static int getGreenCount() {
        int greenCount = 0;
        for (ballColor color : carouselSlots) {
            if (color == ballColor.GREEN) {
                greenCount++;
            }
        }

        return greenCount;
    }




    private static ballColor getBallColorPG(RevColorSensorV3 sensor){
        if (sensor.getDistance(DistanceUnit.CM) < hasBallThreshold) {
            return hueToBallColor(JavaUtil.colorToHue(sensor.getNormalizedColors().toColor()));
        }
        return hueToBallColor(200);
    }

    public static final void updateAll(){
        carouselSlots[0] = getBallColorPG(intakeColorSensor);
        carouselSlots[1] = getBallColorPG(leftBackColorSensor);
        int greenCount = getGreenCount();

        if(greenCount == 0){
            carouselSlots[2] = ballColor.GREEN;
        }
        else{
            carouselSlots[2] = ballColor.PURPLE;
        }
    };


}

