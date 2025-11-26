package org.firstinspires.ftc.teamcode.subsystems.indivdual;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.limelightvision.LLFieldMap;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

import dev.nextftc.core.commands.Command;
import dev.nextftc.core.commands.utility.InstantCommand;
import dev.nextftc.core.commands.utility.LambdaCommand;
import dev.nextftc.core.subsystems.Subsystem;
import dev.nextftc.ftc.ActiveOpMode;

public class limelight implements Subsystem {
    private Limelight3A limelight3A;
    public static limelight INSTANCE = new limelight();
    private limelight(){}
    public static int obeliskId = 0;


    public final Command scanObelisk = new InstantCommand(() -> {
         List<LLResultTypes.FiducialResult> results = limelight3A.getLatestResult().getFiducialResults();
        obeliskId = (results != null && !results.isEmpty())  ? results.get(0).getFiducialId() : 21;
    });

    public final Command stopLimelight = new InstantCommand(() ->{
       limelight3A.stop();
    });

    public final Command startLimelight = new InstantCommand(() -> {
        limelight3A.pipelineSwitch(0);
        limelight3A.start();
    });

    @Override
    public void initialize(){
        limelight3A = ActiveOpMode.hardwareMap().get(Limelight3A.class, "limelight");
    }
}