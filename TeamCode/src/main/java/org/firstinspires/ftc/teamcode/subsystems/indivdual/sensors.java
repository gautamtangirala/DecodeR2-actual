package org.firstinspires.ftc.teamcode.subsystems.indivdual;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class sensors implements Subsystem {
    public static final sensors INSTANCE = new sensors();
    private sensors() {}

    public RevColorSensorV3 intakeColorSensor;
    public RevColorSensorV3 flywheelColorSensor;
    public RevColorSensorV3 humanPlayerColorSensor;

    @Override
    public void initialize() {
        // initialization logic (runs on init)
        flywheelColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "flywheelColorSensor");
        intakeColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "intakeColorSensor");
        humanPlayerColorSensor = ActiveOpMode.hardwareMap().get(RevColorSensorV3.class, "humanPlayerColorSensor");
    }
}

