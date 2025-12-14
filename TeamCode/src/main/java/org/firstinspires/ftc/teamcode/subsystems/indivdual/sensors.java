package org.firstinspires.ftc.teamcode.subsystems.indivdual;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class sensors implements Subsystem {
    public static final sensors INSTANCE = new sensors();
    private sensors() {}

    public RevColorSensorV3 intakeColorSensor;
    public RevColorSensorV3 backCarouselColorSensor;
    private NormalizedRGBA intakeRGBA;
    private NormalizedRGBA backCarouselRGBA;
    public static double intakeHue;
    public static double backCarouselHue;

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
        intakeColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "intakeColor");
        backCarouselColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "backCarouselColor");
    }

    private ballColor hueToBallColor(double hue) {

        if (hue > 60 && hue < 120) {
            return ballColor.GREEN;
        }

        return ballColor.PURPLE;
    }




    public final Command updateColor = new InstantCommand(() -> {

        intakeRGBA = intakeColorSensor.getNormalizedColors();
        intakeHue = JavaUtil.colorToHue(intakeRGBA.toColor());

        backCarouselRGBA = backCarouselColorSensor.getNormalizedColors();
        backCarouselHue = JavaUtil.colorToHue(backCarouselRGBA.toColor());


        carouselSlots[0] = hueToBallColor(intakeHue);
        carouselSlots[1] = hueToBallColor(backCarouselHue);


        int purpleCount = 0;
        int greenCount = 0;

        for (int i = 0; i < 2; i++) {
            if (carouselSlots[i] == ballColor.PURPLE) purpleCount++;
            if (carouselSlots[i] == ballColor.GREEN) greenCount++;
        }


        if (purpleCount == 2) {
            carouselSlots[2] = ballColor.GREEN;
        } else if (greenCount == 1 && purpleCount == 1) {
            carouselSlots[2] = ballColor.PURPLE;
        } else {
            carouselSlots[2] = ballColor.PURPLE;
        }
    });

}

