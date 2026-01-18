package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.carouselSlots;
import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.getBallCounts;
import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.intakeBeamBroken;
import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.intakeSensorReading;
//import static org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors.intakeHasBall;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.hood;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.kicker;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.sensors;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.spindexer;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.limelight;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.traction;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.delays.WaitUntil;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;


public class robot extends SubsystemGroup {
    public static robot INSTANCE = new robot();
    private robot(){
        super(
                spindexer.INSTANCE,
                flywheel.INSTANCE,
                intake.INSTANCE,
                limelight.INSTANCE,
                sensors.INSTANCE,
                kicker.INSTANCE,
                traction.INSTANCE,
                hood.INSTANCE
        );
    }

    public final Command prepCarouselToMove(){
        return new NullCommand();
    }

    public final Command rotateCarousel(){
        return new NullCommand();
    }

    public final Command greenToFlywheel(){
       /*
       prepCarouselToMove
        if flywheel artifact = green
            return null
        else if human player artifact = green
            return ID2Flywheel
        else
            return ID1Flywheel
        */


        return new NullCommand();
    }

    public final Command purpleToFlywheel(){
        /*
        prepCarouselToMove
        if flywheel artifact = purple
            return null
        else if human player artifact = purple
            return ID2Flywheel
        else
            return ID1Flywheel
        */

        return new NullCommand();
    }

    public final Command prepIntake(){
        /*
        sequential group

        prepCarouselToMove

        if intake artifact not there
        return null
        if human player artifact not there
        return ID2Intake
        else
        return ID3Intake

        open intake gate
         */
        return new NullCommand();
    }

    public final Command prepHumanPlayer(){
        /*
        sequential group

        prepCarouselToMove

        if hp artifact not there
        return null
        if intake artifact not there
        return ID1HumanPlayer
        else
        return ID3HumanPlayer

        open human player gate
         */
        return new NullCommand();
    }

    public final Command kickToFlywheel(){
        /*
        open carousel gate

        kick ball
         */
        return new NullCommand();
    }


    public final Command closeStopperStopFlywheel(){
        return new NullCommand();
    }




    public final Command shootPurple(){
        return new SequentialGroup(
                prepCarouselToMove(),
                purpleToFlywheel(),
                kickToFlywheel(),
                new Delay(0.25),
                prepCarouselToMove(),
                new Delay(1)
        );
    }

    public final Command shootGreen(){
        return new SequentialGroup(
                prepCarouselToMove(),
                greenToFlywheel(),
                kickToFlywheel(),
                new Delay(0.2)
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
                new Delay(0.2),
                new InstantCommand(() -> spindexer.nextSlot()),
                new Delay(0.5),
                new InstantCommand(() -> spindexer.nextSlot()),
                new Delay(0.5),
                new InstantCommand(() -> spindexer.nextSlot()),
                new Delay(0.5),
                kicker.INSTANCE.kickerDown,
                new InstantCommand(() -> shooting = false));
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
                            spindexer.nextSlot();
                        }
                        intakeHasBallLastLoop = intakeHasBall;
                    }
            )
            .setStop(interrupted -> {intake.INSTANCE.stop.schedule();})
            .setIsDone(() -> stopIntake);

    public final Command stopIntaking = new InstantCommand(() -> stopIntake = true);


    public final Command sort(int obeliskId){
        return new SequentialGroup(
                new InstantCommand(() -> spindexer.nextSlot()),
                new Delay(0.1),
                new InstantCommand(() -> sensors.updateAll()),
                new InstantCommand(() -> {
                    int[] ballCounts = getBallCounts();
                    int greenCount = ballCounts[0];
                    int purpleCount = ballCounts[1];
                    if (greenCount >= 1 && obeliskId != 0){
                        if(obeliskId == 21){ //GPP
                            if(carouselSlots[0] == sensors.ballColor.GREEN){
                                spindexer.nextSlot();
                            }
                            else if(carouselSlots[2] == sensors.ballColor.GREEN){
                                spindexer.nextSlot();
                                spindexer.nextSlot();
                            }
                        }
                        else if(obeliskId == 22){ //PGP
                            if(carouselSlots[2] == sensors.ballColor.GREEN){
                                spindexer.nextSlot();
                            }
                            else if(carouselSlots[1] == sensors.ballColor.GREEN){
                                spindexer.nextSlot();
                                spindexer.nextSlot();
                            }
                        }
                        else if(obeliskId == 23){ //PPG
                            if(carouselSlots[1] == sensors.ballColor.GREEN){
                                spindexer.nextSlot();
                            }
                            else if(carouselSlots[0] == sensors.ballColor.GREEN){
                                spindexer.nextSlot();
                                spindexer.nextSlot();
                            }
                        }
                    }
                }),
                new InstantCommand(() -> spindexer.betweenSlots())
        );
    }




}
