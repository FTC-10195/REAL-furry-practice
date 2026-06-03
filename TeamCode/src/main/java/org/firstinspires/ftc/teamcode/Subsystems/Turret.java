package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Localizer;

@Config
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
    private double heading = 0;
    private double targetRadians;
    private double targetTicks;


    public double calculateRadiansToTicks(double radians){
        return defaultPos + Math.toDegrees(radians)/maxDegrees;
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
        targetRadians = theta - heading;
        if (targetRadians > Math.PI){
            targetRadians = -((2 * Math.PI) - targetRadians);
        }
        targetTicks = calculateRadiansToTicks(targetRadians);
    }

    public void initiate(HardwareMap hardwareMap){
        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");
    }
    public void ftcDashboardUpdate(TelemetryPacket telemetryPacket){
        telemetryPacket.put("Target Angle", targetRadians);
        telemetryPacket.put("Target Ticks", targetTicks);
    }
    public void update(Telemetry telemetry){
        heading = Localizer.getPose().getHeading();
        switch (currentTurretState) {
            case AIM:
                calculateTurretAngle();
                if (targetTicks > maxPos){
                    targetTicks = maxPos;
                }
                if (targetTicks < minPos){
                    targetTicks = minPos;
                }
                rightServo.setPosition(targetTicks);
                leftServo.setPosition(targetTicks);
                break;
            case MANUAL:
                rightServo.setPosition(defaultPos);
                leftServo.setPosition(defaultPos);
                break;
        }
        telemetry.addData("TURRET TELEMETRY", 0);
        telemetry.addData("Target Radians", targetRadians);
        telemetry.addData("Target Degrees", Math.toDegrees(targetRadians));
        telemetry.addData("Target Ticks", targetTicks);

    }
}
