package org.firstinspires.ftc.teamcode.opmode.tele;


import static org.firstinspires.ftc.teamcode.fieldConstants.redGoalPose;
import static org.firstinspires.ftc.teamcode.fieldConstants.robotPose;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockD;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockI;
import static org.firstinspires.ftc.teamcode.opmode.tuners.headingLockTuner.headingLockP;
import static org.firstinspires.ftc.teamcode.subsystems.robot.shooting;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.kicker;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.limelight;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.old.contSpindexer;
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
                new SubsystemComponent(robot.INSTANCE),
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
        traction.INSTANCE.retract.schedule();
        follower().update();
    }

    @Override
    public void onStartButtonPressed() {
        //holonomic drive with heading lock when right bumper held
        Gamepads.gamepad1().rightTrigger().greaterThan(0).whenTrue(() -> headingLockEnabled = true).whenFalse(() -> headingLockEnabled = false);
        Gamepads.gamepad1().leftTrigger().greaterThan(0).whenBecomesTrue(() -> {tractionEnabled = true; traction.INSTANCE.deploy.schedule();}).whenBecomesFalse(() -> {tractionEnabled = false; traction.INSTANCE.retract.schedule();});
        Gamepads.gamepad1().y().toggleOnBecomesTrue().whenBecomesTrue(() -> {tractionEnabled = true; traction.INSTANCE.tip.schedule();}).whenBecomesFalse(() -> {tractionEnabled = false; traction.INSTANCE.retract.schedule();});
        DriverControlledCommand driverControlled = new PedroDriverControlled(
                Gamepads.gamepad1().leftStickY().negate(),
                () -> tractionEnabled ? 0 : -Gamepads.gamepad1().leftStickX().get(),
                () -> headingLockEnabled ? headingLockControl.calculate(new KineticState(follower().getHeading(), follower().getAngularVelocity())) : -Gamepads.gamepad1().rightStickX().get()
        );
        driverControlled.schedule();
        BindingManager.update();



        //intake commands
        Gamepads.gamepad2().leftTrigger().greaterThan(0).whenBecomesTrue(robot.INSTANCE.startIntaking).whenBecomesFalse(robot.INSTANCE.stopIntaking);
        Gamepads.gamepad2().rightTrigger().greaterThan(0).whenBecomesTrue(intake.INSTANCE.out).whenBecomesFalse(intake.INSTANCE.stop);


        //shoot all
        Gamepads.gamepad2().dpadDown().and(() -> !shooting).whenBecomesTrue(() -> robot.INSTANCE.shootAll().schedule());
        Gamepads.gamepad2().x().toggleOnBecomesTrue().whenBecomesTrue(kicker.INSTANCE.kickerUp).whenBecomesFalse(kicker.INSTANCE.kickerDown);

        //sorting commands
        Gamepads.gamepad2().rightBumper().whenBecomesTrue(() -> contSpindexer.nextSlot());
        Gamepads.gamepad2().b().whenBecomesTrue(() -> robot.INSTANCE.sort(limelight.obeliskId));


        //flywheel
        Gamepads.gamepad2().dpadLeft().toggleOnBecomesTrue().whenBecomesTrue(() -> flywheel.spinAtSpeed(1400)).whenBecomesFalse(flywheel.INSTANCE.stop);
        Gamepads.gamepad2().dpadRight().toggleOnBecomesTrue().whenBecomesTrue(() -> flywheel.spinAtSpeed(1100)).whenBecomesFalse(flywheel.INSTANCE.stop);

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
        BindingManager.reset();

    }
}
