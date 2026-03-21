package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
@Config
public class Flywheel {
    public enum States{
        ON,
        OFF;
    }
    DcMotor FW1;
    DcMotor FW2;
    public States currentStates = States.OFF;
    public boolean IsReady = false;
    public static double defaultPower = 0.6;
    public static double increment = 0.1;
    public double power = defaultPower;
    public void initiate(HardwareMap hardwareMap){
        FW1 = hardwareMap.dcMotor.get("FW1");
        FW2 = hardwareMap.dcMotor.get("FW2");
        FW2.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void setCurrentStates(States newState){currentStates = newState;}
    public States getCurrentStates() {
        return currentStates;
    }
    public void update(){
        switch (currentStates){
            case ON:
                FW1.setPower(power);
                FW2.setPower(power);
                break;
            case OFF:
                FW1.setPower(0);
                FW2.setPower(0);
        }
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

