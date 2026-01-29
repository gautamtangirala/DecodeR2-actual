package org.firstinspires.ftc.teamcode.opmode.tuners;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.posSpindexer;

import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Configurable
@TeleOp(name = "Spindexer Tuner", group = "Tuners")
public class spindexerTuner extends NextFTCOpMode {

    public spindexerTuner(){
        addComponents(
                new SubsystemComponent(posSpindexer.INSTANCE),
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


    private final ElapsedTime timer = new ElapsedTime();
    public static long loopDelay = 20;
    @Override
    public void onUpdate() {
        posSpindexer.INSTANCE.setSpindexerPos(target).schedule();
        telemetry.addData("loop time", timer.seconds());
        telemetry.update();
        timer.reset();
        sleep(loopDelay);

    }

    @Override
    public void onStop(){
    }
}
