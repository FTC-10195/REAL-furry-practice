package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Limelight;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.robotcore.external.Telemetry;
@Config
public class Localizer {
    public static Pose blueGoal = new Pose(0,144);
    public static Pose redGoal = new Pose(144,144);
    public static Follower follower;
    public static Pose startingPose = new Pose(72,72,0); //See ExampleAuto to understand how to use this
    public static Pose robotPose = startingPose;
    public static Limelight limelight = new Limelight();
    public static double limelightWeighting = 0.1;
    public static double distanceNoise = 10;
    public static double angularNoise = .25;
    public void initiate(HardwareMap hardwareMap) {
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startingPose);
        follower.update();
        limelight.initiate(hardwareMap);
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
    public static double angleSumForPedroCoordinates(double a1, double a2){
        if (Math.signum(a1/a2) == -1){
            double a; //negative
            double b; //positive
            if (Math.signum(a1) == -1){
                a = a1;
                b = a2;
            }else{
                a = a2;
                b = a1;
            }
            double sum = Math.abs(a) + b;
            if (Math.abs(a) < b){
                sum = sum * -1;
            }
            return sum;

        }else{
            return a1 + a2;
        }
    }
    Pose prevLimelightPose = null;
    public void sensorFusion(){
        double weight = limelightWeighting;
        if (!limelight.canSeeTag) {
            weight = 0;
            prevLimelightPose = null;
            return;
        }
        if (prevLimelightPose != null) {
            if (limelight.getLimelightPose().distanceFrom(prevLimelightPose) > distanceNoise){
                weight = 0;
            }
            if (Math.abs(limelight.getLimelightPose().getHeading() - prevLimelightPose.getHeading()) > angularNoise){
                weight = 0;
            }
        }
        prevLimelightPose = limelight.getLimelightPose();
        if (weight == 0){
            return;
        }

        double x = (limelight.getLimelightPose().getX() * weight) + (follower.getPose().getX() * (1 - weight));
        double y = (limelight.getLimelightPose().getY() * weight) + (follower.getPose().getY() * (1 - weight));
        double heading = angleSumForPedroCoordinates((limelight.getLimelightPose().getHeading() * weight), (follower.getPose().getHeading() * (1 - weight)));
        follower.setPose(new Pose(x, y, heading));
    }


    public void update(Telemetry telemetry){
        follower.update();
        limelight.update(telemetry);
        sensorFusion();
        robotPose = follower.getPose();
        telemetry.addData("Robot Pos", robotPose);

    }
    public void ftcDashboardUpdate(TelemetryPacket telemetryPacket){
            telemetryPacket.put("Position", robotPose);
    }

}
