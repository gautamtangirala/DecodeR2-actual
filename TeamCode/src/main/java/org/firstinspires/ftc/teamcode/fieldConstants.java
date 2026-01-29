package org.firstinspires.ftc.teamcode;


import com.pedropathing.geometry.Pose;

public class fieldConstants {
    public static final Pose blueGoalPose = new Pose(12, 132);
    public static final Pose blueCloseScorePose = new Pose(48,84,Math.toRadians(135));
    public static final Pose blueFarScorePose = new Pose(56,14,Math.toRadians(111));


    public static final Pose redGoalPose = blueGoalPose.mirror();
    public static final Pose redCloseScorePose = blueCloseScorePose.mirror();
    public static final Pose redFarScorePose = blueFarScorePose.mirror();

    public static Pose robotPose = new Pose();
    public static double goalDist = 0;
}
