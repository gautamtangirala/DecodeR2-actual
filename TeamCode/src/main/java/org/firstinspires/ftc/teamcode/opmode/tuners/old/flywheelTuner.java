package org.firstinspires.ftc.teamcode.opmode.tuners.old;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.hood;

import dev.nextftc.control.ControlSystem;
import dev.nextftc.control.KineticState;
import dev.nextftc.control.feedforward.BasicFeedforward;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.ActiveOpMode;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;
import dev.nextftc.hardware.controllable.MotorGroup;
import dev.nextftc.hardware.impl.MotorEx;

@Disabled
@Configurable
@TeleOp(name = "Flywheel Tuner", group = "Tuners")
public class flywheelTuner extends NextFTCOpMode {

    public flywheelTuner(){
        addComponents(
                new SubsystemComponent(hood.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }

    private MotorGroup flywheelMotor = new MotorGroup(new MotorEx("flywheel1"), new MotorEx("flywheel2"));

    public static double target = 0;
    public static double flywheelP = 0.00375;
    public static double flywheelI = 0;
    public static double flywheelD = 0;
    private ControlSystem flywheelControl = ControlSystem.builder()
            .velPid(flywheelP, flywheelI, flywheelD)
            .feedforward(new BasicFeedforward(0.00048, 0.0004, 0.000145))
            .build();


    @Override
    public void onInit (){
    }


    @Override
    public void onStartButtonPressed() {
      }


    @Override
    public void onUpdate() {
        flywheelControl.setGoal(new KineticState(0, target));
        flywheelMotor.setPower(flywheelControl.calculate(new KineticState(flywheelMotor.getCurrentPosition(), flywheelMotor.getVelocity())));
        ActiveOpMode.telemetry().addData("flywheel velocity: ", flywheelMotor.getVelocity());
        ActiveOpMode.telemetry().addData("flywheel calculated power: ",flywheelControl.calculate(new KineticState(flywheelMotor.getCurrentPosition(), flywheelMotor.getVelocity())));
        telemetry.addData("flywheel target vel: ", target);
        telemetry.update();

    }

    @Override
    public void onStop(){}
}
