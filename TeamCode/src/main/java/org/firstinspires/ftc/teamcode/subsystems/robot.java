package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.subsystems.indivdual.carousel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.flywheel;
import org.firstinspires.ftc.teamcode.subsystems.indivdual.intake;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.NullCommand;
import dev.nextftc.core.subsystems.SubsystemGroup;

public class robot extends SubsystemGroup {
    public static robot INSTANCE = new robot();
    private robot(){
        super(
                carousel.INSTANCE,
                flywheel.INSTANCE,
                intake.INSTANCE
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


}
