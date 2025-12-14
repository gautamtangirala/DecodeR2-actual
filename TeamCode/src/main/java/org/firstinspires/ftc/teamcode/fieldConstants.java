package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;

public class fieldConstants {
    public static final Pose blueGoalPose = new Pose(0, 144);
    public static final Pose blueScorePose = new Pose(56,84,Math.toRadians(135));

    public static final Pose redGoalPose = blueGoalPose.mirror();
    public static final Pose redScorePose = blueScorePose.mirror();

    public static Pose robotPose = new Pose(0,0,0);

}
