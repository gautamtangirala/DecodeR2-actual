package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.carousel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.limelight;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.conditionals.SwitchCommand;
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
     /*
     close all gates
      */
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

    public final Command shootPurple(){
        return new SequentialGroup(
                prepCarouselToMove(),
                purpleToFlywheel(),
                kickToFlywheel()
        );
    }

    public final Command shootGreen(){
        return new SequentialGroup(
                prepCarouselToMove(),
                greenToFlywheel(),
                kickToFlywheel()
        );
    }


    public final Command GPPmotif(){
        return new SequentialGroup(
                shootGreen(),
                shootPurple(),
                shootPurple()
        );
    }

    public final Command PGPmotif(){
        return new SequentialGroup(
                shootPurple(),
                shootGreen(),
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

    public final Command scoreMotif(){
        return new SwitchCommand<>(() -> limelight.obeliskId)
                .withCase(21, GPPmotif())
                .withCase(22, PGPmotif())
                .withCase(23, PPGmotif())
                .withCase(0,  PPGmotif());
    }



}
