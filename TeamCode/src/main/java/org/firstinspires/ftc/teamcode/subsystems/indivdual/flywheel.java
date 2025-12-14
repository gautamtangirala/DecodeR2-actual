package org.firstinspires.ftc.teamcode.subsystems.indivdual;



import android.health.connect.datatypes.ExerciseRoute;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedforward.BasicFeedforward;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.controllable.RunToVelocity;
import dev.nextftc.hardware.impl.MotorEx;
import dev.nextftc.hardware.impl.ServoEx;
import dev.nextftc.hardware.positionable.SetPosition;
import dev.nextftc.hardware.powerable.SetPower;

public class flywheel implements Subsystem {
    public static final flywheel INSTANCE = new flywheel();
    private flywheel() {}




    //flywheel
    private MotorGroup flywheelMotor = new MotorGroup(new MotorEx("flywheel1"), new MotorEx("flywheel2"));

    public ControlSystem flywheelControl = ControlSystem.builder()
            .velPid(0.00375, 0, 0)
            .feedforward(new BasicFeedforward(0.00048, 0.0004, 0.000145))
            .build();


 /*   public final Command shootBall(double x, double y, double angle){
        double ballVelocity = 0;
        return new RunToVelocity(controlSystem, ballVelocity, -1).requires(this);
    } */

    //public final Command start = new SetPower(flywheelMotor, 1).requires(flywheelMotor);
    public final Command start = new InstantCommand(() -> flywheelControl.setGoal(new KineticState(0,1800))).requires(flywheelMotor);

    public final Command spinAtSpeed(double goal){
        return new InstantCommand(() -> flywheelControl.setGoal(new KineticState(0, goal))).requires(flywheelMotor);
    }

    //public final Command stop = new SetPower(flywheelMotor, 0).requires(flywheelMotor);
    public final Command stop = new InstantCommand(() -> flywheelControl.setGoal(new KineticState(0,0))).requires(flywheelMotor);


    //kicker
    private ServoEx kickServo = new ServoEx("kicker");

    private final double kickBallInPos = 1;
    public final Command kickBallIn = new SetPosition(kickServo, kickBallInPos).requires(kickServo);

    private final double resetKickerPos = 0;
    public final Command resetKicker = new SetPosition(kickServo, resetKickerPos).requires(kickServo);

    private ServoEx stopServo = new ServoEx("stopper");
    private final double openStopperPos = 0.4;
    public final Command openStopper = new SetPosition(stopServo, openStopperPos).requires(stopServo);

    private final double closeStopperPos = 1;
    public final Command closeStopper = new SetPosition(stopServo, closeStopperPos).requires(stopServo);


    @Override
    public void initialize(){
        stop.invoke();
        closeStopper.invoke();
        resetKicker.invoke();
    }

    @Override
    public void periodic(){
        flywheelMotor.setPower(flywheelControl.calculate(new KineticState(flywheelMotor.getCurrentPosition(), flywheelMotor.getVelocity())));
        ActiveOpMode.telemetry().addData("flywheel velocity: ", flywheelMotor.getVelocity());
        ActiveOpMode.telemetry().addData("flywheel calculated power: ",flywheelControl.calculate(new KineticState(flywheelMotor.getCurrentPosition(), flywheelMotor.getVelocity())));
    }
}
