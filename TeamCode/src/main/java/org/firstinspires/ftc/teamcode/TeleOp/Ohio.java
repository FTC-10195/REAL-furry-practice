package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Subsystems.Intake.States.OFF;
import static org.firstinspires.ftc.teamcode.Subsystems.Intake.States.ON;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;

@TeleOp
public class Ohio extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();
        Drivetrain drivetrain = new Drivetrain();
        drivetrain.initiate(hardwareMap);
        Intake intake = new Intake();
        intake.initiate(hardwareMap);
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            if (gamepad1.rightBumperWasPressed()){
                switch (intake.getState()){
                    case OFF:
                        intake.setState(ON);
                        break;
                    case ON:
                        intake.setState(OFF);
                        break;

                }

            }

            drivetrain.update(x, y, rx);
        }
    }
}

