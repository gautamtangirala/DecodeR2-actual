package org.firstinspires.ftc.teamcode.subsystems.indivdual;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.subsystems.Subsystem;

public class sensors implements Subsystem {
    public static final sensors INSTANCE = new sensors();
    private sensors() {}

    private ColorRangeSensor intakeColorSensor = hardwareMap.get(RevColorSensorV3.class, "intakeColorSensor");
    private ColorRangeSensor flywheelColorSensor = hardwareMap.get(RevColorSensorV3.class, "flywheelColorSensor");
    private ColorRangeSensor humanPlayerColorSensor = hardwareMap.get(RevColorSensorV3.class, "humanPlayerColorSensor");

    
}

