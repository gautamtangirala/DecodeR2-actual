package org.firstinspires.ftc.teamcode.opmode.auto.redAuto;


import static com.pedropathing.paths.HeadingInterpolator.lazy;
import static com.pedropathing.paths.HeadingInterpolator.linear;
import static org.firstinspires.ftc.teamcode.fieldConstants.blueFarScorePose;
import static org.firstinspires.ftc.teamcode.fieldConstants.redCloseScorePose;
import static org.firstinspires.ftc.teamcode.fieldConstants.robotPose;
import static dev.nextftc.extensions.pedro.PedroComponent.follower;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.limelight;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.traction;
import org.firstinspires.ftc.teamcode.subsystems.robot;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelDeadlineGroup;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.extensions.pedro.FollowPath;
import dev.nextftc.extensions.pedro.PedroComponent;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;

@Autonomous(name = "close Red",  preselectTeleOp = "Red Side Teleop", group = "Red")
public class redClose extends NextFTCOpMode {
    public redClose() {
        addComponents(
                new SubsystemComponent(robot.INSTANCE, traction.INSTANCE),
                BulkReadComponent.INSTANCE,
                new PedroComponent(Constants::createFollower)
        );
    }


    private final Pose startPose = new Pose(30, 128, Math.toRadians(180)).mirror();
    private final Pose scanObeliskPose = new Pose(60, 100, Math.toRadians(70)).mirror();
    private final Pose scorePose = redCloseScorePose;

    private final double prepPickupBallsX = 44;
    private final Pose prepPickup1Pose = new Pose(prepPickupBallsX, 84, Math.toRadians(180)).mirror();
    private final Pose prepPickup2Pose = new Pose(prepPickupBallsX, 56, Math.toRadians(180)).mirror();
    private final Pose prepPickup3Pose = new Pose(prepPickupBallsX, 32, Math.toRadians(180)).mirror();

    private final double pickupBallsX = prepPickupBallsX - 35;
    private final Pose pickup1Pose = new Pose(18, prepPickup1Pose.getY(), Math.toRadians(180)).mirror();
    private final Pose pickup2Pose = new Pose(pickupBallsX, prepPickup2Pose.getY(), Math.toRadians(180)).mirror();
    private final Pose pickup3Pose = new Pose(pickupBallsX, prepPickup3Pose.getY(), Math.toRadians(180)).mirror();

    private final Pose pickup1ControlPoint = new Pose(scorePose.getX(), pickup1Pose.getY()).mirror();
    private final Pose pickup2ControlPoint = new Pose(scorePose.getX(), pickup2Pose.getY()).mirror();
    private final Pose pickup3ControPoint = new Pose(scorePose.getX(), pickup3Pose.getY()).mirror();

    private final Pose pushGatePose = new Pose(14, 63, Math.toRadians(180)).mirror();
    private final Pose leavePose = new Pose(24, 84, Math.toRadians(180)).mirror();

    public PathChain scoreLine, pushGate, leave, spikeMark1Pickup, spikeMark2Pickup, spikeMark3Pickup, spikeMark1ScoreCurve, spikeMark2ScoreCurve, spikeMark3ScoreCurve;



    public void buildPaths() {

        scoreLine = follower().pathBuilder()
                .addPath(new BezierLine(() -> follower().getPose(), scorePose))
                .setHeadingInterpolation(lazy(() -> linear(follower().getHeading(), scorePose.getHeading(),0.8)))
                .build();

        pushGate = follower().pathBuilder()
                .addPath(new BezierLine(() -> follower().getPose(), pushGatePose))
                .setHeadingInterpolation(lazy(() -> linear(follower().getHeading(), scorePose.getHeading(), 0.8)))
                .build();

        leave = follower().pathBuilder()
                .addPath(new BezierLine(() -> follower().getPose(), leavePose))
                .setHeadingInterpolation(lazy(() -> linear(follower().getHeading(), leavePose.getHeading(),0.8)))
                .build();

        spikeMark1Pickup = follower().pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup1Pose.getHeading())
                .addPath(new BezierLine(prepPickup1Pose, pickup1Pose))
                .setConstantHeadingInterpolation(prepPickup1Pose.getHeading())
                .build();

        spikeMark1ScoreCurve = follower().pathBuilder()
                .addPath(new BezierCurve(pickup1Pose, pickup1ControlPoint, scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading(), 0.8)
                .build();

        spikeMark2ScoreCurve = follower().pathBuilder()
                .addPath(new BezierCurve(pickup2Pose, pickup2ControlPoint, scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading(), 0.8)
                .build();

        spikeMark3ScoreCurve = follower().pathBuilder()
                .addPath(new BezierCurve(pickup3Pose, pickup3ControPoint, scorePose))
                .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading(), 0.8)
                .build();



        spikeMark2Pickup = follower().pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup2Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup2Pose.getHeading())
                .addPath(new BezierLine(prepPickup2Pose, pickup2Pose))
                .setConstantHeadingInterpolation(prepPickup2Pose.getHeading())
                .build();

        spikeMark3Pickup = follower().pathBuilder()
                .addPath(new BezierLine(scorePose, prepPickup3Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), prepPickup3Pose.getHeading())
                .addPath(new BezierLine(prepPickup3Pose, pickup3Pose))
                .setConstantHeadingInterpolation(prepPickup3Pose.getHeading())
                .build();
    }


    @Override public void onInit() {
        buildPaths();
        limelight.startLimelight();
        limelight.obeliskId = 0;
        follower().setStartingPose(startPose);
    }


    @Override public void onWaitForStart() {
    }


    private Command auto(){
        return new SequentialGroup(
                new ParallelGroup(new FollowPath(scoreLine), flywheel.INSTANCE.startClose),
                robot.INSTANCE.shootAll(),
                new ParallelDeadlineGroup(new FollowPath(spikeMark1Pickup), robot.INSTANCE.startIntaking, flywheel.INSTANCE.stop),
                new Delay(0.5),
                new ParallelGroup(new FollowPath(spikeMark1ScoreCurve), flywheel.INSTANCE.startClose),
                robot.INSTANCE.stopIntaking,
                robot.INSTANCE.shootAll(),
                new ParallelDeadlineGroup(new FollowPath(spikeMark2Pickup), robot.INSTANCE.startIntaking, flywheel.INSTANCE.stop),
                new Delay(0.5),
                new ParallelGroup(new FollowPath(spikeMark2ScoreCurve), flywheel.INSTANCE.startClose),
                robot.INSTANCE.stopIntaking,
                robot.INSTANCE.shootAll(),
                new ParallelGroup(new FollowPath(leave), flywheel.INSTANCE.stop)
        );
    }

    @Override public void onStartButtonPressed() {
        auto().schedule();
    }

    @Override public void onUpdate() {
        if(limelight.obeliskId == 0){
            limelight.scanObelisk();
        }
        robotPose = follower().getPose();
        telemetry.addData("obelisk ID", limelight.obeliskId);
        telemetry.addData("current pose", robotPose);
        telemetry.update();
    }
    @Override public void onStop() {
        limelight.stopLimelight();
        if(limelight.obeliskId == 0){
            limelight.obeliskId = 21;
        }
        flywheel.INSTANCE.stop.schedule();
        robot.INSTANCE.startIntaking.stop(true);
    }
}