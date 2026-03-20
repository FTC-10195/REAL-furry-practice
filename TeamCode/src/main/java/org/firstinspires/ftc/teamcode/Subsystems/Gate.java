package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Gate {
    public enum States {
        OPEN,
        CLOSE
    }

    States currentStates = States.CLOSE;
    Servo Gate;
    public double openPosition = 0.7;
    public double closePosition = 0.5;
    public void initiate(HardwareMap hardwareMap) {
        Gate = hardwareMap.servo.get("gate");
    }

    public void setCurrentState(States newState) {
        currentStates = newState;
    }

    public States getCurrentState() {
        return currentStates;
    }
    public void update(){
        switch (currentStates){
            case OPEN:
                Gate.setPosition(openPosition);
                break;
            case CLOSE:
                Gate.setPosition(closePosition);
                break;
        }
    }




}
