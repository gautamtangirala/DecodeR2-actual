package org.firstinspires.ftc.teamcode.opmode.tuners;


import static org.firstinspires.ftc.teamcode.subsystems.indivdual.spindexer.targetAngle;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.spindexer;
import org.firstinspires.ftc.teamcode.subsystems.robot;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Configurable
@TeleOp(name = "Spindexer Tuner", group = "Tuners")
public class carouselTuner extends NextFTCOpMode {

    public carouselTuner(){
        addComponents(
                new SubsystemComponent(spindexer.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

   public static double target = 0;
   public static double spindexerP = 0.3;
   public static double spindexerD = 0.005;

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

        targetAngle = target;
        telemetry.addData("loop time", timer.seconds());
        telemetry.update();
        timer.reset();
        sleep(loopDelay);

    }

    @Override
    public void onStop(){
    }
}
