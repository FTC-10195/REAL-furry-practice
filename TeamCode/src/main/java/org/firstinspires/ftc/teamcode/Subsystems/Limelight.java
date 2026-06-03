package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

/* DO NOT PUSH
NO NOT PUSH
DO NOT PUSH
DO NOT PUSH
DO NOT PUSH
 */
@Config
public class Limelight {

    public enum Target {
        RED,
        BLUE,
        NONE;
    }
    Target currentTarget = Target.NONE;
    Limelight3A limelight;
    Pose pose = new Pose();

    public void setTarget(Target target) {
        currentTarget = target;
    }

    public Target getTarget() {
        return currentTarget;
    }


    public Pose getLimelightPose(){
        return pose;
    }
    public boolean canSeeTag = false;
    public void translateLLtoPP(Pose3D limelightPose){
        double x = limelightPose.getPosition().toUnit(DistanceUnit.INCH).y + 72;
        double y = (limelightPose.getPosition().toUnit(DistanceUnit.INCH).x * -1) + 72;
        double heading = limelightPose.getOrientation().getYaw(AngleUnit.RADIANS);
        if (heading < 0){
            heading += (2 * Math.PI);
        }
        heading -= (Math.PI/2);

        pose = new Pose(x,y,heading);
    }

    public static int blueID = 20;
    public static int redID = 24;
    public double output = 0;
    public static double p = 0.02;

    public void initiate(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public void update(Telemetry telemetry) {
        canSeeTag = false;
        LLResult result = limelight.getLatestResult();
        if (!result.isValid()){
            output = 0;
            return;
        }
        for (int i = 0; i < result.getFiducialResults().size(); i++) {
            LLResultTypes.FiducialResult ficidual = result.getFiducialResults().get(i);
            int id = ficidual.getFiducialId();
            if (id == blueID || currentTarget == Target.BLUE) {
                result.getTx();
                output = p * result.getTx();
            }
            if (id == redID || currentTarget == Target.RED) {
                result.getTx();
                output = p * result.getTx();
            }
            if (id == blueID || id == redID){
                canSeeTag = true;
            }
        }


        telemetry.addData("output", output);
        telemetry.addData("target", getTarget());
        telemetry.addData("Limelight pose", result.getBotpose().getPosition().toUnit(DistanceUnit.INCH));
        telemetry.addData("Limelight Heading", result.getBotpose().getOrientation().getYaw(AngleUnit.RADIANS));
        translateLLtoPP(result.getBotpose());
        telemetry.addData("Pedro pose", pose);

    }
    public double getOutput () {
        return  output;
    }
}
