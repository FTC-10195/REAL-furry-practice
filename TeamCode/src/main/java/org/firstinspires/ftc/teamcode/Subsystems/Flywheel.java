package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Localizer;

@Config
public class Flywheel {
    public enum States{
        MANUAL,
        PASSIVE;
    }
    DcMotorEx FW1;
    DcMotorEx FW2;
    VoltageSensor voltageSensor;
    public States currentStates = States.PASSIVE;
    public boolean IsReady = false;
    public static double defaultPower = 0.6;
    public static double increment = 0.1;
    public double power = defaultPower;
    public static double targetVelocity = 1250;
    public static double kF = 0.00041;
    public static double kP = 0.002;
    public static double m = 5.2931;
    public static double b = 763.8155;
    private double deltaY = 0;
    private double deltaX = 0;
    private double distance = 0;
    public double pid(double velocity, double targetVelocity){
        return (targetVelocity - velocity) * kP;
    }
    public double feedForward(double targetVelocity, double voltage){
        return targetVelocity * kF * 12 / voltage;
    }
    public double bangBang(double velocity,double targetVelocity){
        if(velocity > targetVelocity){
            return 0;
        }else{
            return 1;
        }

    }
    public double velocityLinearRegression(){
        double y = m*distance + b;
        if (distance < 80){
            y = 1280;
        }
        return y;
    }
    public void calculateDistanceFromGoal(){
            deltaX = Localizer.getGoalPosition().getX() - Localizer.getPose().getX();
            deltaY = Localizer.getGoalPosition().getY() - Localizer.getPose().getY();
            distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
    }

    public void initiate(HardwareMap hardwareMap){
        FW1 = hardwareMap.get(DcMotorEx.class,"FW1");
        FW2 = hardwareMap.get(DcMotorEx.class,"FW2");
        FW1.setDirection(DcMotorSimple.Direction.REVERSE);
        FW1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        FW2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        voltageSensor = hardwareMap.voltageSensor.iterator().next();
    }
    public States getCurrentStates() {
        return currentStates;
    }
    public void update(){
        calculateDistanceFromGoal();
        switch (currentStates){
            case MANUAL:
                FW1.setPower(feedForward(velocityLinearRegression(), voltageSensor.getVoltage()) + pid(FW1.getVelocity(), velocityLinearRegression()));
                break;
            case PASSIVE:
                FW1.setPower(defaultPower);
        }
        FW2.setPower(FW1.getPower());
    }

    public void status(Telemetry telemetry){
        telemetry.addLine("FLYWHEEL");
        telemetry.addData("Power",FW1.getPower());
        telemetry.addData("Velocity",FW1.getVelocity());
        telemetry.addData("VoltageSensor",voltageSensor.getVoltage());
        telemetry.addData("Distance From Goal", distance);
        telemetry.addData("Flywheel Power (NOT VELOCITY)", FW1.getPower());
    }
    public void increase(){
        power = power + increment;
    }
    public void decrease(){
        power = power - increment;
    }
    public void setState(States newState){
        currentStates = newState;
    }
}

