package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.JoystickNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.MultiInputNode;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

@TeleOp
public class ExampleTeleop extends OpMode {
    private MovementManager driver;
    private ManipulationManager hands;
    private InputManager input;

    @Override
    public void init() {
        FeatureManager.setIsOpModeRunning(true);
        telemetry = new TelemetryManager(telemetry, this);
        FeatureManager.logger.setBackend(telemetry.log());

        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr");
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        driver = new MovementManager(fl, fr, br, bl);

        hands = new ManipulationManager(new CRServo[] {}, new String[] {}, new Servo[] {}, new String[] {}, new DcMotor[] {}, new String[] {});

        input = new InputManager(gamepad1, gamepad2);

        input.registerInput("drivingControls",
            new MultiInputNode(
                new JoystickNode("left_stick_x"),
                new JoystickNode("left_stick_y"),
                new JoystickNode("right_stick_x")
            )
        );
    }

    @Override
    public void loop() {
        input.update();
        driver.driveOmni(input.getFloatArrayOfInput("drivingControls"));
        telemetry.update();
    }

    public void stop() {
        FeatureManager.setIsOpModeRunning(false);
    }
}
