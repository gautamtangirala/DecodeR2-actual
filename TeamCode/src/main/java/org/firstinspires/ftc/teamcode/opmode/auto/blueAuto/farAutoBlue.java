package org.firstinspires.ftc.teamcode.opmode.auto.blueAuto;


import static org.firstinspires.ftc.teamcode.fieldConstants.blueGoalPose;
import static org.firstinspires.ftc.teamcode.fieldConstants.blueScorePose;
import static org.firstinspires.ftc.teamcode.fieldConstants.robotPose;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.limelight;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.traction;
import org.firstinspires.ftc.teamcode.subsystems.robot;

import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.core.units.Angle;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.extensions.pedro.TurnTo;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "Far Blue Side Auto",  preselectTeleOp = "Blue Side Teleop")
public class farAutoBlue extends NextFTCOpMode {
    public farAutoBlue() {
        addComponents(
                new SubsystemComponent(robot.INSTANCE, traction.INSTANCE),
                BulkReadComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }


    private final Pose startPose = new Pose(56, 8, Math.toRadians(90));
    private final Pose scanObeliskPose = new Pose(60,100,Math.toRadians(70));
    private final Pose scorePose = blueScorePose;

    private final double prepPickupBallsX = 44;
    private final Pose prepPickup1Pose = new Pose(prepPickupBallsX, 84, Math.toRadians(180));
    private final Pose prepPickup2Pose = new Pose(prepPickupBallsX, 60, Math.toRadians(180));
    private final Pose prepPickup3Pose = new Pose(prepPickupBallsX, 36, Math.toRadians(180));
    private final double pickupBallsX = prepPickupBallsX-16;
    private final Pose pickup1Pose = new Pose(pickupBallsX, 84, Math.toRadians(180));
    private final Pose pickup2Pose = new Pose(pickupBallsX, 60, Math.toRadians(180));
    private final Pose pickup3Pose = new Pose(pickupBallsX, 36, Math.toRadians(180));

    private final Pose leavePose = new Pose(56, 50, Math.toRadians(180));

    public PathChain scanObelisk, preloads, prepPickup1, pickup1, scorePickup1, prepPickup2, pickup2, scorePickup2, prepPickup3, pickup3, scorePickup3,  leave;
    public Command scorePreloads;

    public void buildPaths() {
        preloads = follower().pathBuilder()
                .addPath(new BezierLine(startPose, scorePose))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();

        prepPickup1 = follower().pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup1Pose.getHeading())
                .build();

        pickup1 = follower().pathBuilder()
                .addPath(new BezierLine(prepPickup1Pose, pickup1Pose))
                .setLinearHeadingInterpolation(prepPickup1Pose.getHeading(), pickup1Pose.getHeading())
                .build();

        scorePickup1 = follower().pathBuilder()
                .addPath(new BezierLine(pickup1Pose, scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
                .build();

        prepPickup2 = follower().pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup2Pose.getHeading())
                .build();

        pickup2 = follower().pathBuilder()
                .addPath(new BezierLine(prepPickup2Pose, pickup2Pose))
                .setLinearHeadingInterpolation(prepPickup2Pose.getHeading(), pickup2Pose.getHeading())
                .build();

        scorePickup2 = follower().pathBuilder()
                .addPath(new BezierLine(pickup2Pose, scorePose))
                .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
                .build();

        prepPickup3 = follower().pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup3Pose.getHeading())
                .build();

        pickup3 = follower().pathBuilder()
                .addPath(new BezierLine(prepPickup3Pose, pickup3Pose))
                .setLinearHeadingInterpolation(prepPickup3Pose.getHeading(), pickup3Pose.getHeading())
                .build();

        scorePickup3 = follower().pathBuilder()
                .addPath(new BezierLine(pickup3Pose, scorePose))
                .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
                .build();

        leave = follower().pathBuilder()
                .addPath(new BezierLine(scorePose, leavePose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), leavePose.getHeading())
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
               new ParallelGroup(new FollowPath(preloads), robot.INSTANCE.openStopperStartFlywheel()),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.closeStopperStopFlywheel(),

                new ParallelGroup(new FollowPath(prepPickup1), intake.INSTANCE.in),
                new FollowPath(pickup1),//run intake and allow balls into carousel
                intake.INSTANCE.stop,
                new ParallelGroup(new FollowPath(scorePickup1), robot.INSTANCE.openStopperStartFlywheel()),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                new Delay(0.5),

                robot.INSTANCE.closeStopperStopFlywheel(),
                new ParallelGroup(new FollowPath(prepPickup2), intake.INSTANCE.in),
                new FollowPath(pickup2), //run intake and allow balls into carousel
                intake.INSTANCE.stop,
                new ParallelGroup(new FollowPath(scorePickup2), robot.INSTANCE.openStopperStartFlywheel()),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                robot.INSTANCE.shootPurple(),
                new Delay(0.5),


                robot.INSTANCE.closeStopperStopFlywheel(),
                new ParallelGroup(new FollowPath(prepPickup3), intake.INSTANCE.in)
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
