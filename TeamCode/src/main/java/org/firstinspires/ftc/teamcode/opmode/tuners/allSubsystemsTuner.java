package org.firstinspires.ftc.teamcode.opmode.tuners;


import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.intakeColorSensor;
//import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.intakeHasBall;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.posSpindexer;
import org.firstinspires.ftc.teamcode.subsystems.robot;

import dev.nextftc.bindings.BindingManager;
import dev.nextftc.core.components.BindingsComponent;
import dev.nextftc.core.components.SubsystemComponent;
import dev.nextftc.ftc.Gamepads;
import dev.nextftc.ftc.NextFTCOpMode;
import dev.nextftc.ftc.components.BulkReadComponent;


@Configurable
@TeleOp(name = "Subsystems Tester", group = "Tuners")
public class allSubsystemsTuner extends NextFTCOpMode {

    public allSubsystemsTuner(){
        addComponents(
                new SubsystemComponent(robot.INSTANCE),
                BulkReadComponent.INSTANCE,
                BindingsComponent.INSTANCE
        );
    }


    @Override
    public void onInit (){
        BindingManager.reset();
    }


    @Override
    public void onStartButtonPressed() {
        Gamepads.gamepad1().a().whenBecomesTrue(robot.INSTANCE.startIntaking).whenBecomesFalse(robot.INSTANCE.stopIntaking);
        Gamepads.gamepad1().y().whenBecomesTrue(() -> posSpindexer.INSTANCE.nextSlot().schedule());
        Gamepads.gamepad1().x().and(() -> !robot.shooting).whenBecomesTrue(() -> robot.INSTANCE.shootAll().schedule());
        Gamepads.gamepad1().dpadUp().toggleOnBecomesTrue().whenBecomesTrue(flywheel.INSTANCE.startFar).whenBecomesFalse(flywheel.INSTANCE.stop);
        Gamepads.gamepad1().b().whenBecomesTrue(() -> robot.INSTANCE.sort(21).schedule());
    }



    private final ElapsedTime timer = new ElapsedTime();
    public static long loopDelay = 20;
    public static double gain = 0;
    @Override
    public void onUpdate() {

        telemetry.addData("intake distance: ", intakeColorSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("intake hue: ", JavaUtil.colorToHue(intakeColorSensor.getNormalizedColors().toColor()));
        telemetry.addLine();
        telemetry.addData("loop time: ", timer.seconds());
        telemetry.update();
        timer.reset();

        BindingManager.update();
    }

    @Override
    public void onStop(){
    }
}
