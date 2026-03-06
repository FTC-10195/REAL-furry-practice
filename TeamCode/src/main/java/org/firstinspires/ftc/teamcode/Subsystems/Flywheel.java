package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Flywheel {
    public enum States{
        ON,
        OFF;
    }
    DcMotor FW1;
    DcMotor FW2;
    public States currentstates = States.OFF;
    public boolean IsReady = false;
    public static double Speed = 1;
    public void initiate(HardwareMap hardwareMap){
        FW1 = hardwareMap.dcMotor.get("FW1");
        FW2 = hardwareMap.dcMotor.get("FW2");
    }
    public void update(){
        switch (currentstates){
            case ON:
                FW1.setPower(Speed);
                FW2.setPower(Speed);
                break;
            case OFF:
                FW1.setPower(0);
                FW2.setPower(0);
        }
    }
    public void setState(States newState){
        currentstates = newState;
    }
}
