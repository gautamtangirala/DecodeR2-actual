package org.firstinspires.ftc.teamcode.subsystems.indivdual;

import static dev.nextftc.bindings.Bindings.button;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import dev.nextftc.bindings.Button;
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

    public static ballColor[] carouselSlots = {
            ballColor.EMPTY,
            ballColor.EMPTY,
            ballColor.EMPTY
    };


    @Override
    public void initialize() {
        // initialization logic (runs on init)
        intakeColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "intakeColorSensor");
        leftBackColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "leftBackColorSensor");
    }

    @Override
    public void periodic(){
        ActiveOpMode.telemetry().addData("1: ", carouselSlots[0]);
        ActiveOpMode.telemetry().addData("2: ", carouselSlots[1]);
        ActiveOpMode.telemetry().addData("3: ", carouselSlots[2]);
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

    private static ballColor getBallColorPG(RevColorSensorV3 sensor){
        if (sensor.getDistance(DistanceUnit.CM) < hasBallThreshold) {
            return hueToBallColor(JavaUtil.colorToHue(sensor.getNormalizedColors().toColor()));
        }
        return hueToBallColor(200);
    }

    public static final void updateAll(){
        carouselSlots[0] = getBallColorPG(intakeColorSensor);
        carouselSlots[1] = getBallColorPG(leftBackColorSensor);

        carouselSlots[2] = ballColor.EMPTY;
        int[] ballCounts = getBallCounts();
        int greenCount = ballCounts[0];
        int purpleCount = ballCounts[1];

        if(greenCount == 0){
            carouselSlots[1] = ballColor.GREEN;
        }
        else{
            carouselSlots[1] = ballColor.PURPLE;
        }
    };


    public static ballColor intakeSensorReading = ballColor.EMPTY;
    public static boolean intakeBeamBroken = false;

    public static final void updateIntake(){
        intakeSensorReading = getBallColor(intakeColorSensor);
        intakeBeamBroken = intakeSensorReading != ballColor.EMPTY;
    }


    public static int[] getBallCounts() {
        int greenCount = 0;
        int purpleCount = 0;

        for (ballColor color : carouselSlots) {
            if (color == ballColor.GREEN) {
                greenCount++;
            } else if (color == ballColor.PURPLE) {
                purpleCount++;
            }
        }

        return new int[]{greenCount, purpleCount};
    }
}

