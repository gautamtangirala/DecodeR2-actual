package org.firstinspires.ftc.teamcode.opmode.tuners;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.traction;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Configurable
@TeleOp(name = "Traction Wheel Tuner", group = "Tuners")
public class tractionWheelTuner extends NextFTCOpMode {

    public tractionWheelTuner(){
        addComponents(
                new SubsystemComponent(traction.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    public static double rightTarget = 0;
    public static double leftTarget = 0;


    @Override
    public void onInit (){
    }


    @Override
    public void onStartButtonPressed() {
      }


    @Override
    public void onUpdate() {
        traction.INSTANCE.setLeftPos(leftTarget).run();
        traction.INSTANCE.setRightPos(rightTarget).run();
        telemetry.update();

    }

    @Override
    public void onStop(){
    }
}
