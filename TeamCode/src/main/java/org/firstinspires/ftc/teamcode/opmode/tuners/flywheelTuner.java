package org.firstinspires.ftc.teamcode.opmode.tuners;


import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;

import java.nio.channels.FileLockInterruptionException;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedforward.BasicFeedforward;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;


@Configurable
@TeleOp(name = "Flywheel Tuner", group = "Tuners")
public class flywheelTuner extends NextFTCOpMode {

    public flywheelTuner(){
        addComponents(
                new SubsystemComponent(flywheel.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    public static double target = 0;
    public static double flywheelP = 0.00375;
    public static double flywheelI = 0;
    public static double flywheelD = 0;
    private ControlSystem flywheelControl = ControlSystem.builder()
            .velPid(flywheelP, flywheelI, flywheelD)
            .feedforward(new BasicFeedforward(0.00048, 0.0004, 0.000145))
            .build();


    @Override
    public void onInit (){
    }


    @Override
    public void onStartButtonPressed() {
      }


    @Override
    public void onUpdate() {
        flywheelControl.setGoal(new KineticState(0, target));
        telemetry.update();

    }

    @Override
    public void onStop(){
        flywheel.INSTANCE.stop.invoke();
    }
}
