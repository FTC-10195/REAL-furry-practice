package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
@Config
public class Gate {
    public enum States {
        OPEN,
        CLOSE
    }

    States currentStates = States.CLOSE;
    Servo Gate;
    public static double openPosition = 0.2;
    public static double closePosition = 0.45;
    public void initiate(HardwareMap hardwareMap) {
        Gate = hardwareMap.servo.get("gate");
    }

    public void setCurrentState(States newState) {
        currentStates = newState;
    }

    public States getCurrentState() {
        return currentStates;
    }
    public void increase(){ closePosition = closePosition + 0.1;};
    public void decrease(){closePosition = closePosition - 0.1;};
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
