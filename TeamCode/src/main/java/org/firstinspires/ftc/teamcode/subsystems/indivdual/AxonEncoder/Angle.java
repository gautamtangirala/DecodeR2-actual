package org.firstinspires.ftc.teamcode.subsystems.indivdual.AxonEncoder;

/**
 * Provides utilities for wrapping angles.
 */
public class Angle {
    private static final double TAU = 2 * Math.PI;

    public static double norm(double angle) {
        double modifiedAngle = angle % TAU;

        modifiedAngle = (modifiedAngle + TAU) % TAU;

        return modifiedAngle;
    }

    public static double normDelta(double angleDelta) {
        double modifiedAngleDelta = norm(angleDelta + TAU);

        if (modifiedAngleDelta > Math.PI) {
            modifiedAngleDelta -= TAU;
        }

        return modifiedAngleDelta;
    }
}