package org.firstinspires.ftc.teamcode.opmode.tele;


import static org.firstinspires.ftc.teamcode.fieldConstants.blueGoalPose;
import static org.firstinspires.ftc.teamcode.fieldConstants.blueScorePose;
import static org.firstinspires.ftc.teamcode.fieldConstants.robotPose;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockD;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockI;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockP;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
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
import dev.nextftc.hardware.driving.MecanumDriverControlled;
import dev.nextftc.hardware.impl.MotorEx;


@Configurable
@TeleOp(name = "Blue Side Teleop")
public class fullTeleBlue extends NextFTCOpMode {
    public fullTeleBlue(){
        addComponents(
                new PedroComponent(Constants::createFollower),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }



    private static PIDCoefficients headingLockCoefficients = new PIDCoefficients(headingLockP, headingLockI, headingLockD);
    private ControlSystem headingLockControl = ControlSystem.builder()
            .angular(AngleType.RADIANS, fb -> fb.posPid(headingLockCoefficients))
            .build();
    private boolean headingLockEnabled = false;

    @Override
    public void onInit(){
        follower().setStartingPose(robotPose);
        Gamepads.gamepad1().rightStickX();
        follower().update();
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


    KineticState goal;
    @Override
    public void onUpdate() {
        goal = new KineticState(Math.atan2(blueGoalPose.getY() - follower().getPose().getY() , blueGoalPose.getX() - follower().getPose().getX()));
        headingLockControl.setGoal(goal);
        telemetry.addData("heading lock: ", headingLockEnabled);
        telemetry.addData("robot pose: ", follower().getPose());
        telemetry.addData("goal:", goal.component1());
        telemetry.update();
        robotPose = follower().getPose();
    }
}
