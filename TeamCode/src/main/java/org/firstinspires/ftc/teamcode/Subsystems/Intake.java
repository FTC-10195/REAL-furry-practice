package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.sun.tools.javac.util.SharedNameTable;

public class Intake {
    public enum States {
        ON,
        OFF,
        EJECT;
    }

    public States currentStates = States.OFF;
    public static double intakePower = 0.6;
    public static double ejectPower = -0.6;
    DcMotor Intake;
    DcMotor Transfer;

    public void initiate(HardwareMap hardwareMap) {
        Intake = hardwareMap.dcMotor.get("Intake");
        Transfer = hardwareMap.dcMotor.get("Transfer");
        Intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        Transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void update() {
        switch (currentStates) {
            case ON:
                Intake.setPower(intakePower);
                Transfer.setPower(intakePower);
                break;
            case OFF:
                Intake.setPower(0);
                Transfer.setPower(0);
                break;
            case EJECT:
                Intake.setPower(ejectPower);
                Transfer.setPower(ejectPower);
                break;
        }
    }

    public void setState(States newState) {
        currentStates = newState;

    }
    public States getState(){
        return currentStates;
    }
}
