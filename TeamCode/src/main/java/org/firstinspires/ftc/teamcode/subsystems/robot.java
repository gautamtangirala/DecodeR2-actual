package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.carouselSlots;
import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.getGreenCount;
import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.intakeBeamBroken;
import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.intakeSensorReading;
//import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.intakeHasBall;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.hood;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.kicker;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.posSpindexer;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.limelight;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.traction;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;
import dev.nextftc.ftc.ActiveOpMode;


public class robot extends SubsystemGroup {
    public static robot INSTANCE = new robot();
    private robot(){
        super(
                posSpindexer.INSTANCE,
                flywheel.INSTANCE,
                intake.INSTANCE,
                limelight.INSTANCE,
                sensors.INSTANCE,
                kicker.INSTANCE,
                traction.INSTANCE,
                hood.INSTANCE
        );
    }


    public static boolean shooting;
    public final Command shootAll() {
        //Ramp up
        //turn 360
        //Ramp down
        return new SequentialGroup(
                new InstantCommand(() -> shooting = true),
                kicker.INSTANCE.kickerUp,
                new Delay(0.4),
                posSpindexer.INSTANCE.shootAll,
                new Delay(0.6),
//                new InstantCommand(() -> posSpindexer.INSTANCE.nextSlot().schedule()),
//                new Delay(0.5),
//                new InstantCommand(() -> posSpindexer.INSTANCE.nextSlot().schedule()),
//                new Delay(0.5),
//                new InstantCommand(() -> posSpindexer.INSTANCE.nextSlot().schedule()),
//                new Delay(0.5),
                kicker.INSTANCE.kickerDown,
                new InstantCommand(() -> {shooting = false;
                    carouselSlots[0] = sensors.ballColor.EMPTY;
                    carouselSlots[1] = sensors.ballColor.EMPTY;
                    carouselSlots[2] = sensors.ballColor.EMPTY;
                }),
                posSpindexer.INSTANCE.ID0Intake);
    }


    boolean intakeHasBall;
    boolean intakeHasBallLastLoop = false;
    public static boolean stopIntake;

    public final Command startIntaking = new LambdaCommand()
            .setStart(() -> {
                stopIntake = false;
                intake.INSTANCE.in.schedule();
                kicker.INSTANCE.kickerDown.schedule();
            })
            .setUpdate(
                    () ->{
                        sensors.updateIntake();
                        intakeHasBall = intakeBeamBroken;
                        if(intakeHasBall && !intakeHasBallLastLoop){
                            carouselSlots[0] = intakeSensorReading;
                            posSpindexer.INSTANCE.nextSlot().schedule();
                        }
                        intakeHasBallLastLoop = intakeHasBall;
                    }
            )
            .setStop(interrupted -> {intake.INSTANCE.stop.schedule();
                intakeHasBallLastLoop = false;})
            .setIsDone(() -> stopIntake);

    public final Command stopIntaking = new InstantCommand(() -> stopIntake = true);


    public final Command sort(int obeliskId){
        return new SequentialGroup(
                new InstantCommand(() -> ActiveOpMode.telemetry().addLine("sorting")),
                posSpindexer.INSTANCE.ID0Intake,
                new Delay(0.3),
                new InstantCommand(() -> sensors.updateAll()),
                new InstantCommand(() -> {
                    int greenCount = getGreenCount();
                    if (greenCount >= 1 && obeliskId != 0) {
                        if (obeliskId == 21) { //GPP
                            if (carouselSlots[0] == sensors.ballColor.GREEN) {
                                posSpindexer.INSTANCE.nextSlot().schedule();
                            } else if (carouselSlots[1] == sensors.ballColor.GREEN) {
                                posSpindexer.INSTANCE.nextSlot().schedule();
                                posSpindexer.INSTANCE.nextSlot().schedule();
                            }
                        } else if (obeliskId == 22) { //PGP
                            if (carouselSlots[1] == sensors.ballColor.GREEN) {
                                posSpindexer.INSTANCE.nextSlot().schedule();
                            } else if (carouselSlots[2] == sensors.ballColor.GREEN) {
                                posSpindexer.INSTANCE.nextSlot().schedule();
                                posSpindexer.INSTANCE.nextSlot().schedule();
                            }
                        } else if (obeliskId == 23) { //PPG
                            if (carouselSlots[2] == sensors.ballColor.GREEN) {
                                posSpindexer.INSTANCE.nextSlot().schedule();
                            } else if (carouselSlots[0] == sensors.ballColor.GREEN) {
                                posSpindexer.INSTANCE.nextSlot().schedule();
                                posSpindexer.INSTANCE.nextSlot().schedule();
                            }
                        }
                    }
                }));
    }




}
