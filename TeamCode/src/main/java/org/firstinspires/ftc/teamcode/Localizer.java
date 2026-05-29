package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.HeadingInterpolator;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Localizer {
    public static Pose blueGoal = new Pose(0,144);
    public static Pose redGoal = new Pose(144,144);
    public static Follower follower;
    public static Pose startingPose = new Pose(72,72,0); //See ExampleAuto to understand how to use this
    public static Pose robotPose;
    public void initiate(HardwareMap hardwareMap) {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose == null ? new Pose() : startingPose);
        follower.update();
    }
    public static Pose getPose(){
        return robotPose;
    }
    public static Pose getGoalPosition(){
        if (TeamColor.currentTeamColor == TeamColor.Team.RED){
            return redGoal;
        }
        return blueGoal;
    }

    public void update(Telemetry telemetry){
        follower.update();
        robotPose = follower.getPose();
        telemetry.addData("Position", robotPose);
    }

}
