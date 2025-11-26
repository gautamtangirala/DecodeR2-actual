package org.firstinspires.ftc.teamcode.opmode.tuners;


import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;


@Configurable
@TeleOp(name = "Heading Lock Tuner", group = "Tuners")
public class headingLockTuner extends NextFTCOpMode {

    public headingLockTuner(){
        addComponents(
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }


    public static double headingLockP = 1;
    public static double headingLockI = 0;
    public static double headingLockD = 0;
    public static double target = 0;

    private static PIDCoefficients headingLockCoefficients = new PIDCoefficients(headingLockP, headingLockI, headingLockD);
    private ControlSystem headingLockControl = ControlSystem.builder()
            .angular(AngleType.RADIANS, fb -> fb.posPid(headingLockCoefficients))
            .build();
    private boolean headingLockEnabled = false;

    @Override
    public void onInit(){
        follower().setStartingPose(new Pose());
        Gamepads.gamepad1().rightStickX();
    }


    @Override
    public void onStartButtonPressed() {
        Gamepads.gamepad1().rightBumper().whenTrue(() -> headingLockEnabled = true).whenFalse(() -> headingLockEnabled = false);
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY(),
                Gamepads.gamepad1().leftStickX(),
                () -> headingLockEnabled ? headingLockControl.calculate(new KineticState(follower().getHeading(), follower().getAngularVelocity())) : Gamepads.gamepad1().rightStickX().get()
        );
        driverControlled.schedule();
        BindingManager.update();
    }


    @Override
    public void onUpdate() {
        headingLockControl.setGoal(new KineticState(target));
        telemetry.addData("heading lock: ", headingLockEnabled);
        telemetry.addData("robot pose: ", follower().getPose());
        telemetry.addData("heading: ", follower().getHeading());
        telemetry.addData("target: ", target);
        telemetry.update();

    }
}
