package org.firstinspires.ftc.teamcode.opmode.tuners.old;


import static org.firstinspires.ftc.teamcode.subsystems.indivdual.hood.currentCalcHoodAngle;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.hood;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Disabled
@Configurable
@TeleOp(name = "Hood Tuner", group = "Tuners")
public class hoodTuner extends NextFTCOpMode {

    public hoodTuner(){
        addComponents(
                new SubsystemComponent(hood.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    public static double target = currentCalcHoodAngle;


    @Override
    public void onInit (){
    }


    @Override
    public void onStartButtonPressed() {
      }


    @Override
    public void onUpdate() {
        currentCalcHoodAngle = target;
        telemetry.addData("hood target: ", target);
        telemetry.update();
    }

    @Override
    public void onStop(){
    }
}
