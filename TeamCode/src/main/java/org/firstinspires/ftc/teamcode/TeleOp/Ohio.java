package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Subsystems.Gate.States.CLOSE;
import static org.firstinspires.ftc.teamcode.Subsystems.Gate.States.OPEN;
import static org.firstinspires.ftc.teamcode.Subsystems.Intake.States.EJECT;
import static org.firstinspires.ftc.teamcode.Subsystems.Intake.States.OFF;
import static org.firstinspires.ftc.teamcode.Subsystems.Intake.States.ON;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.Subsystems.Gate;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Timer;

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
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            if (gamepad1.rightBumperWasPressed()) {
                switch (intake.getCurrentState()) {
                    case OFF:
                        intake.setState(ON);
                        break;
                    case ON:
                        intake.setState(OFF);
                        break;

                }
            }
            if (gamepad1.leftBumperWasPressed()){
                intake.setState(OFF);
                flywheel.setState(Flywheel.States.OFF);
                gate.setCurrentState(CLOSE);
            }
            if (gamepad1.square){
                intake.setState(EJECT);
            }
            if (gamepad1.squareWasReleased()){
                intake.setState(OFF);
            }
            if (gamepad1.rightTriggerWasPressed()) {
                switch (gate.getCurrentState()) {
                    case OPEN:
                        gate.setCurrentState(CLOSE);
                        break;
                    case CLOSE:
                        gate.setCurrentState(OPEN);
                        break;
                }
            }
            if (gamepad1.leftTriggerWasPressed()) {
                switch (flywheel.getCurrentStates()) {
                    case ON:
                        flywheel.setCurrentStates(Flywheel.States.OFF);
                        break;
                    case OFF:
                        flywheel.setCurrentStates(Flywheel.States.ON);
                        break;
                }
            }
            if (gamepad1.dpadUpWasPressed()){
                flywheel.increase();
            }
            if (gamepad1.dpadDownWasPressed()){
                flywheel.decrease();
            }


            drivetrain.update(x, y, rx);
            intake.update();
            flywheel.update();
            gate.update();
        }
    }
}

