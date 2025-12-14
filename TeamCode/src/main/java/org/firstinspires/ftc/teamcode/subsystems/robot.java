package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.carousel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.limelight;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.SwitchCommand;
import dev.nextftc.core.commands.delays.Delay;
import dev.nextftc.core.commands.groups.ParallelGroup;
import dev.nextftc.core.commands.groups.SequentialGroup;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;


public class robot extends SubsystemGroup {
    public static robot INSTANCE = new robot();
    private robot(){
        super(
                carousel.INSTANCE,
                flywheel.INSTANCE,
                intake.INSTANCE,
                limelight.INSTANCE
        );
    }

    public final Command prepCarouselToMove(){
        return flywheel.INSTANCE.resetKicker;
    }

    public final Command rotateCarousel(){
        return carousel.INSTANCE.nextPosition();
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
        return flywheel.INSTANCE.kickBallIn;
    }

    public final Command openStopperStartFlywheel(){
        return new ParallelGroup(flywheel.INSTANCE.start, flywheel.INSTANCE.openStopper);
    }

    public final Command closeStopperStopFlywheel(){
        return new ParallelGroup(flywheel.INSTANCE.stop, flywheel.INSTANCE.closeStopper);
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


    public final Command GPPmotif(){
        return new SequentialGroup(
                shootGreen(),
                new Delay(0.1),
                shootPurple(),
                new Delay(0.1),
                shootPurple()
        );
    }

    public final Command PGPmotif(){
        return new SequentialGroup(
                shootPurple(),
                new Delay(0.1),
                shootGreen(),
                new Delay(0.1),
                shootPurple()
        );
    }

    public final Command PPGmotif(){
        return new SequentialGroup(
              shootPurple(),
              shootPurple(),
              shootGreen()
        );
    }

    public final Command scoreMotif() {
        switch (limelight.obeliskId) {
            case 21:
                return GPPmotif();
            case 22:
                return PGPmotif();
            case 23:
                return PPGmotif();
            default:
                return PPGmotif();  // or whatever default you want
        }
    }



}
