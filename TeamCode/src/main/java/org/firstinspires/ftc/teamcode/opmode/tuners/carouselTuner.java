package org.firstinspires.ftc.teamcode.opmode.tuners;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.carousel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedforward.BasicFeedforward;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Configurable
@TeleOp(name = "Carousel Tuner", group = "Tuners")
public class carouselTuner extends NextFTCOpMode {

    public carouselTuner(){
        addComponents(
                new SubsystemComponent(carousel.INSTANCE),
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
        carousel.INSTANCE.setPos(target).run();
        telemetry.update();

    }

    @Override
    public void onStop(){
    }
}
