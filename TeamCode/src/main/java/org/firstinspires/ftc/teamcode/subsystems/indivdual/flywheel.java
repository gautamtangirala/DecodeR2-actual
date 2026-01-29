package org.firstinspires.ftc.teamcode.subsystems.indivdual;



import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedforward.BasicFeedforward;
import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;

public class flywheel implements Subsystem {
    public static final flywheel INSTANCE = new flywheel();
    private flywheel() {}

    private MotorGroup flywheelMotor = new MotorGroup(new MotorEx("flywheel1"), new MotorEx("flywheel2").reversed());

    public static ControlSystem flywheelControl = ControlSystem.builder()
            .velPid(0.0045, 0, 0)
            .feedforward(new BasicFeedforward(0.00048, 0.0004, 0.000145))
            .build();

    public final Command startFar = new InstantCommand(() -> flywheelControl.setGoal(new KineticState(0,1350))).requires(flywheelMotor);

    public final Command startClose = new InstantCommand(() -> flywheelControl.setGoal(new KineticState(0,1400))).requires(flywheelMotor);


    public static final void spinAtSpeed(double goal){
        flywheelControl.setGoal(new KineticState(0, goal));
    }

    //public final Command stop = new SetPower(flywheelMotor, 0).requires(flywheelMotor);
    public final Command stop = new InstantCommand(() -> flywheelControl.setGoal(new KineticState(0,0))).requires(flywheelMotor);





    @Override
    public void initialize(){
        stop.schedule();
    }

    @Override
    public void periodic(){
        if(flywheelControl.getGoal().getVelocity() == 0){
            flywheelMotor.setPower(0);
        }
        else {
            flywheelMotor.setPower(flywheelControl.calculate(new KineticState(flywheelMotor.getCurrentPosition(), flywheelMotor.getVelocity())));
        }
        ActiveOpMode.telemetry().addLine("Flywheel");
        ActiveOpMode.telemetry().addData("flywheel velocity: ", flywheelMotor.getVelocity());
        ActiveOpMode.telemetry().addData("flywheel target: ", flywheelControl.getGoal().getVelocity());
//        ActiveOpMode.telemetry().addData("flywheel calculated power: ",flywheelControl.calculate(new KineticState(flywheelMotor.getCurrentPosition(), flywheelMotor.getVelocity())));
        ActiveOpMode.telemetry().addLine();

    }
}
