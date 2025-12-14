package org.firstinspires.ftc.teamcode.opmode.tele;


import static org.firstinspires.ftc.teamcode.fieldConstants.redGoalPose;
import static org.firstinspires.ftc.teamcode.fieldConstants.robotPose;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockD;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockI;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockP;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.traction;
import org.firstinspires.ftc.teamcode.subsystems.robot;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedback.AngleType;
import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.PedroDriverControlled;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.driving.DriverControlledCommand;


@Configurable
@TeleOp(name = "Red Side Teleop")
public class fullTeleRed extends NextFTCOpMode {
    public fullTeleRed(){
        addComponents(
                new PedroComponent(Constants::createFollower),
                new SubsystemComponent(robot.INSTANCE, traction.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }



    private static PIDCoefficients headingLockCoefficients = new PIDCoefficients(headingLockP, headingLockI, headingLockD);
    private ControlSystem headingLockControl = ControlSystem.builder()
            .angular(AngleType.RADIANS, fb -> fb.posPid(headingLockCoefficients))
            .build();
    private boolean headingLockEnabled = false;
    private boolean tractionEnabled = false;


    @Override
    public void onInit(){
        BindingManager.reset();
        follower().setStartingPose(robotPose);
        Gamepads.gamepad1().rightStickX();
        Gamepads.gamepad1().leftStickX();
        follower().update();
    }

    @Override
    public void onStartButtonPressed() {
        //holonomic drive with heading lock when right bumper held
        Gamepads.gamepad1().rightTrigger().greaterThan(0).whenTrue(() -> headingLockEnabled = true).whenFalse(() -> headingLockEnabled = false);
        Gamepads.gamepad1().leftTrigger().greaterThan(0).whenTrue(() -> {tractionEnabled = true; traction.INSTANCE.deploy.schedule();}).whenFalse(() -> {tractionEnabled = false; traction.INSTANCE.retract.schedule();});
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                () -> tractionEnabled ? 0 : -Gamepads.gamepad1().leftStickX().get(),
                () -> headingLockEnabled ? headingLockControl.calculate(new KineticState(follower().getHeading(), follower().getAngularVelocity())) : -Gamepads.gamepad1().rightStickX().get()
        );
        driverControlled.schedule();
        BindingManager.update();



        //intake commands
        Gamepads.gamepad2().a().whenTrue(intake.INSTANCE.in).whenFalse(intake.INSTANCE.stop);
        Gamepads.gamepad2().y().whenTrue(intake.INSTANCE.out);


        //kicker
        Gamepads.gamepad2().x().whenBecomesTrue(robot.INSTANCE.kickToFlywheel()).whenBecomesFalse(robot.INSTANCE.prepCarouselToMove());

        //carousel
  //      Gamepads.gamepad2().leftBumper().whenBecomesTrue(carousel.INSTANCE.ID1Intake);
  //      Gamepads.gamepad2().rightBumper().whenBecomesTrue(carousel.INSTANCE.ID2Intake);
  //      Gamepads.gamepad2().dpadLeft().whenBecomesTrue(carousel.INSTANCE.ID3Intake);


        //flywheel
        Gamepads.gamepad2().dpadUp().toggleOnBecomesTrue().whenBecomesTrue(robot.INSTANCE.openStopperStartFlywheel()).whenBecomesFalse(robot.INSTANCE.closeStopperStopFlywheel());

    }


    KineticState goal;
    @Override
    public void onUpdate() {
        goal = new KineticState(Math.atan2(redGoalPose.getY() - follower().getPose().getY() , redGoalPose.getX() - follower().getPose().getX()));
        headingLockControl.setGoal(goal);
        telemetry.addData("heading lock: ", headingLockEnabled);
        telemetry.addData("robot pose: ", follower().getPose());
        telemetry.addData("goal:", goal.component1());
        telemetry.update();
        robotPose = follower().getPose();
    }

    @Override
    public void onStop(){
        intake.INSTANCE.stop.schedule();
        flywheel.INSTANCE.stop.schedule();
        robot.INSTANCE.prepCarouselToMove().schedule();
        BindingManager.reset();
    }
}
