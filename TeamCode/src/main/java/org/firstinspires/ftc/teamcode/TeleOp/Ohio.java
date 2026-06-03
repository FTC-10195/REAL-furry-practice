package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Subsystems.Gate.States.CLOSE;
import static org.firstinspires.ftc.teamcode.Subsystems.Gate.States.OPEN;
import static org.firstinspires.ftc.teamcode.Subsystems.Intake.States.EJECT;
import static org.firstinspires.ftc.teamcode.Subsystems.Intake.States.OFF;
import static org.firstinspires.ftc.teamcode.Subsystems.Intake.States.ON;
import static org.firstinspires.ftc.teamcode.Subsystems.Timer.timer;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.Subsystems.Gate;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Timer;
import org.firstinspires.ftc.teamcode.Subsystems.Turret;
import org.firstinspires.ftc.teamcode.TeamColor;

@TeleOp
public class Ohio extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        Drivetrain drivetrain = new Drivetrain();
        drivetrain.initiate(hardwareMap);
        Intake intake = new Intake();
        intake.initiate(hardwareMap);
        Gate gate = new Gate();
        gate.initiate(hardwareMap);
        Flywheel flywheel = new Flywheel();
        flywheel.initiate(hardwareMap);
        Localizer localizer = new Localizer();
        localizer.initiate(hardwareMap);
        Turret turret = new Turret();
        turret.initiate(hardwareMap);
        TelemetryPacket telemetryPacket = new TelemetryPacket();
        if (isStopRequested()) {
            return;
        }
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            if (gamepad1.leftTriggerWasPressed()) {
                switch (intake.getCurrentState()) {
                    case OFF:
                        intake.setState(ON);
                        break;
                    case ON:
                        intake.setState(OFF);
                        break;

                }
            }
            if (gamepad1.optionsWasPressed()){
                TeamColor.switchTeam();
            }
            if (gamepad1.leftBumperWasPressed()) {
                intake.setState(OFF);
                flywheel.setState(Flywheel.States.OFF);
                gate.setCurrentState(CLOSE);
            }
            if (gamepad1.square) {
                intake.setState(EJECT);
            }
            if (gamepad1.squareWasReleased()) {
                intake.setState(OFF);
            }
            if (gamepad1.triangleWasPressed()){
                switch(turret.currentTurretState){
                    case AIM:
                        turret.setTurretState(Turret.States.MANUAL);
                        break;
                    case MANUAL:
                        turret.setTurretState(Turret.States.AIM);
                        break;
                }
            }
            if (gamepad1.rightTriggerWasPressed()) {
                switch (flywheel.getCurrentStates()) {
                    case MANUAL:
                        intake.setState(ON);
                        gate.setCurrentState(OPEN);
                        Timer.timer.setWaitTime(1100);
                        while (opModeIsActive() && !timer.doneWaiting()) {
                            intake.update();
                            gate.update();
                        }
                        gate.setCurrentState(CLOSE);
                        intake.setState(OFF);
                        gate.update();
                        break;
                    case OFF:
                        flywheel.setState(Flywheel.States.MANUAL);
                        break;
                }
            }

            if (gamepad1.dpadUpWasPressed()) {
                flywheel.increase();
            }
            if (gamepad1.dpadDownWasPressed()) {
                flywheel.decrease();
            }


            drivetrain.update(x, y, rx);
            intake.update();
            flywheel.update();
            gate.update();
            turret.update(telemetry);
            turret.ftcDashboardUpdate(telemetryPacket);
            TeamColor.update(telemetry);
            flywheel.status(telemetry);
            localizer.update(telemetry);
            localizer.ftcDashboardUpdate(telemetryPacket);
            FtcDashboard.getInstance().sendTelemetryPacket(telemetryPacket);
            telemetry.update();
        }
    }

}

