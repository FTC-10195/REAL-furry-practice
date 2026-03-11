package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Gate {
    public enum States {
        OPEN,
        CLOSE
    }

    States currentState = States.CLOSE;
    Servo gate;
    public double openPosition = 0.7;
    public double closePosition = 0.5;
    public void initiate(HardwareMap hardwareMap) {
        gate = hardwareMap.servo.get("gate");

    }

    public void setCurrentState(States newState) {
        currentState = newState;
    }

    public States getCurrentState() {
        return currentState;
    }

    public void shoot() {
        setCurrentState(States.OPEN);
    }

    public void update() {
        if (currentState == States.OPEN){
            gate.setPosition(openPosition);
        }else{
            gate.setPosition(closePosition);
        }
    }


}
