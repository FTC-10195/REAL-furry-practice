
package org.firstinspires.ftc.teamcode.Autonomous;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous
public class SampleAutonomousRun extends OpMode {
    private Localizer localizer = new Localizer();
    private Timer pathTimer, opModeTimer;

    public enum PathState {
        //START POSITION_END POSITION
        //DRIVE > MOVEMENT STATE
        //SHOOT > ATTEMPT TO SCORE THE ARTIFACT
        DRIVE_STARTPOS_SHOOTPOS,
        SHOOT_PRELOAD;
    }

    PathState pathState;
    private final Pose startPose = new Pose(15.47663551401869, 128.0747663551402, Math.toRadians(138));
    private final Pose shootPose = new Pose(47.598599766627764, 95.81796966161026, Math.toRadians(138));
    private final Pose 

    private PathChain driveStartPosShootPos;

    public void buildPath(){
        // put in coordinates for starting pose > ending pose
        driveStartPosShootPos = localizer.follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
    }

    public void statePathUpdate(){
        switch (pathState){
            case DRIVE_STARTPOS_SHOOTPOS:
                localizer.follower.followPath(driveStartPosShootPos, true);
                setPathState(PathState.SHOOT_PRELOAD); //changes to shootpreload and resets timer simulatenously
                break;
            case SHOOT_PRELOAD:
                // check is follower done it's path?
                if (!localizer.follower.isBusy()){
                    // add logic for flywheel
                }
                break;
            default:
                telemetry.addLine("no state commanded");
                break;
        }
    }

    public void setPathState(PathState newState){
        pathState = newState;
        pathTimer.resetTimer();
    }

    @Override
    public void init(){
        pathState = PathState.DRIVE_STARTPOS_SHOOTPOS;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        localizer.initiate(hardwareMap);
        // TODO add any other init mechanisms

        buildPath();
        localizer.follower.setPose(startPose);

    }

    public void start(){
        opModeTimer.resetTimer();
        setPathState(pathState);

    }
    @Override
    public void loop(){
        localizer.update(telemetry);
        localizer.follower.update();
        statePathUpdate();

    }

}
