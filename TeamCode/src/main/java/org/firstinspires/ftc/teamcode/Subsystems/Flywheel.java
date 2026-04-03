package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

@Config
public class Flywheel {
    public enum States{
        ON,
        OFF;
    }
    DcMotorEx FW1;
    DcMotorEx FW2;
    VoltageSensor voltageSensor;
    public States currentStates = States.OFF;
    public boolean IsReady = false;
    public static double defaultPower = 0.6;
    public static double increment = 0.1;
    public double power = defaultPower;
    public static double targetVelocity = 1250;
    public static double kF = 0.00041;
    public static double kP = 0.002;
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
    public void initiate(HardwareMap hardwareMap){
        FW1 = hardwareMap.get(DcMotorEx.class,"FW1");
        FW2 = hardwareMap.get(DcMotorEx.class,"FW2");
        FW1.setDirection(DcMotorSimple.Direction.REVERSE);
        voltageSensor = hardwareMap.voltageSensor.iterator().next();
    }
    public void setCurrentStates(States newState){currentStates = newState;}
    public States getCurrentStates() {
        return currentStates;
    }
    public void update(){
        switch (currentStates){
            case ON:
                FW1.setPower(feedForward(targetVelocity, voltageSensor.getVoltage()) + pid(FW1.getVelocity(), targetVelocity));
                break;
            case OFF:
                FW1.setPower(0);
        }
        FW2.setPower(FW1.getPower());
    }
    public void status(Telemetry telemetry){
        telemetry.addLine("FLYWHEEL");
        telemetry.addData("Power",FW1.getPower());
        telemetry.addData("Velocity",FW1.getVelocity());
        telemetry.addData("VoltageSensor",voltageSensor.getVoltage());
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

