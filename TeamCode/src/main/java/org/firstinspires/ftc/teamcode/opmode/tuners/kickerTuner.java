package org.firstinspires.ftc.teamcode.opmode.tuners;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.kicker;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.traction;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Configurable
@TeleOp(name = "Kicker Tuner", group = "Tuners")
public class kickerTuner extends NextFTCOpMode {

    public kickerTuner(){
        addComponents(
                new SubsystemComponent(kicker.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    public static double target = 0;


    @Override
    public void onInit (){
    }


    @Override
    public void onStartButtonPressed() {
      }


    @Override
    public void onUpdate() {
        kicker.INSTANCE.setKickerPos(target).run();
        telemetry.addData("kicker target: ", target);
        telemetry.update();

    }

    @Override
    public void onStop(){
    }
}
