package org.firstinspires.ftc.teamcode.opmode.auto.redAuto;


import static org.firstinspires.ftc.teamcode.fieldConstants.robotPose;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.limelight;
import org.firstinspires.ftc.teamcode.subsystems.robot;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Close Red Side Auto", preselectTeleOp = "Red Side Teleop")
public class closeAutoRed extends NextFTCOpMode {
    public closeAutoRed() {
        addComponents(
                new SubsystemComponent(robot.INSTANCE,
                        limelight.INSTANCE),
                BulkReadComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }


    private final Pose startPose = new Pose(30, 128, Math.toRadians(180)).mirror();
    private final Pose scorePose = new Pose(61, 133, Math.toRadians(180)).mirror();

    public PathChain scanObelisk, preloads, prepPickup1, pickup1, scorePickup1, prepPickup2, pickup2, scorePickup2, prepPickup3, pickup3, scorePickup3,  leave;

    public void buildPaths() {
        preloads = follower().pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
    }


    @Override public void onInit() {
        buildPaths();
        limelight.obeliskId = 21;
        follower().setStartingPose(startPose);
    }


    @Override public void onWaitForStart() {
    }

    private Command auto(){
        return new SequentialGroup(
                new ParallelGroup(new FollowPath(preloads), flywheel.INSTANCE.spinAtSpeed(1650)),
                flywheel.INSTANCE.openStopper,
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.closeStopperStopFlywheel()
        );
    }

    @Override public void onStartButtonPressed() {
        auto().schedule();
    }
    @Override public void onUpdate() {
        robotPose = follower().getPose();
        telemetry.addData("obelisk ID", limelight.obeliskId);
        telemetry.addData("current pose", robotPose);
        telemetry.update();
    }
    @Override public void onStop() {
        robot.INSTANCE.closeStopperStopFlywheel().schedule();
        flywheel.INSTANCE.resetKicker.schedule();
        intake.INSTANCE.stop.schedule();
    }
}
