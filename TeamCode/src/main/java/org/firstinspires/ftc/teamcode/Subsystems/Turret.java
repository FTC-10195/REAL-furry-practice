package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Localizer;
import com.pedropathing.geometry.Pose;

public class Turret {
    public enum States {
        AIM,
        MANUAL;
    }
    public States currentTurretState = States.AIM;
    Servo rightServo; //dominant
    Servo leftServo;
    public static double defaultPos = 0.51;
    public static double maxDegrees = 327.27;
    public static double minDegrees = 0;
    public static double maxPos = 0.85;
    public static double minPos = 0;


    private double deltaX = 0;
    private double deltaY = 0;
    private double theta = 0;
    private double targetAngle;
    private double targetTicks;

    public double calculateRadiansToTicks(double radians){
        return Math.toDegrees(radians)/maxDegrees;
    }
    public void setTurretState(States newState){
        currentTurretState = newState;
    }
    public States getCurrentTurretState(){
        return currentTurretState;
    }
    public void resetTurretAngle(){
        rightServo.setPosition(defaultPos);
        leftServo.setPosition(defaultPos);
    }

    public void calculateTurretAngle(){
        deltaX = Localizer.getGoalPosition().getX() - Localizer.getPose().getX();
        deltaY = Localizer.getGoalPosition().getY() - Localizer.getPose().getY();
        theta = Math.atan2(deltaY, deltaX); //In Radians
        targetAngle = theta - Localizer.getPose().getHeading();
        targetTicks = calculateRadiansToTicks(targetAngle);
    }

    public void initiate(HardwareMap hardwareMap){
        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");
    }
    public void update(){
        switch (currentTurretState) {
            case AIM:
                calculateTurretAngle();
                rightServo.setPosition(targetTicks);
            case MANUAL:
                rightServo.setPosition(defaultPos);
                break;
        }
        if (targetTicks > maxPos){
            targetTicks = maxPos;
        }
        if (targetTicks < minPos){
            targetTicks = minPos;
        }
        leftServo.setPosition(rightServo.getPosition());

    }
}
