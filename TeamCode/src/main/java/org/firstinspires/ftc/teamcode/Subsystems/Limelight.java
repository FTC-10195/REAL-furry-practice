package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

/* DO NOT PUSH
NO NOT PUSH
DO NOT PUSH
DO NOT PUSH
DO NOT PUSH
 */
public class Limelight {
    public enum State {
        ON,
        OFF;
    }

    public enum Target {
        RED,
        BLUE,
        NONE;
    }

    State currentState = State.OFF;
    Target currentTarget = Target.NONE;
    Limelight3A limelight;

    public void setTarget(Target target) {
        currentTarget = target;
    }

    public Target getTarget() {
        return currentTarget;
    }

    public void setState(State state) {
        currentState = state;
    }

    public State getState() {
        return currentState;
    }

    public static int blueID = 20;
    public static int redID = 24;
    public double output = 0;
    public static double p = 0.1;

    public void initiate(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public void update() {
        LLResult result = limelight.getLatestResult();
        if (currentState == State.OFF) {
            output = 0;
            return;
        }
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
        }


    }
    public double getOutput () {
        return  output;
    }
}
