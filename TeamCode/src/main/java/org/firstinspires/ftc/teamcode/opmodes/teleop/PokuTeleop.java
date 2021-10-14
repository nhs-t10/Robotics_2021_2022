package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.nodes.ButtonNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.JoystickNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.MultiInputNode;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.junit.Test;

@TeleOp
public class PokuTeleop extends OpMode{
    private MovementManager driver;
    private ManipulationManager hands;
    private InputManager input;
    private boolean precision = false;

    @Override
    public void init(){
        FeatureManager.setIsOpModeRunning(true);
        telemetry = new TelemetryManager(telemetry, this, TelemetryManager.BITMASKS.NONE);

        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr");
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        DcMotor toy = hardwareMap.get(DcMotor.class, "toy");

        driver = new MovementManager(fl, fr, br, bl);

        hands = new ManipulationManager(new CRServo[] {}, new String[] {}, new Servo[] {}, new String[] {}, new DcMotor[] {toy}, new String[] {"toy"});

        input = new InputManager(gamepad1, gamepad2);

        input.registerInput("drivingControls",
                new MultiInputNode(
                        new JoystickNode("left_stick_y"),
                        new JoystickNode("left_stick_x"),
                        new JoystickNode("right_stick_x")
                )
        );
        input.registerInput("PrecisionDriving",
                new ButtonNode("b")
        );
        input.registerInput("grinder",
                new ButtonNode("a")
        );
        input.registerInput("taunts",
                new MultiInputNode(
                        new ButtonNode("dpad_up"),
                        new ButtonNode("dpad_left"),
                        new ButtonNode("dpad_right"),
                        new ButtonNode("dpad_down")
                )
        );
    }

    @Override
    public void loop(){
        input.update();

        if (input.getBool("grinder")){
            hands.setMotorPower("toy", 0.4);
        }else{
            hands.setMotorPower("toy", 0);
        }

        driver.driveOmni(input.getFloatArrayOfInput("drivingControls"));
        if (input.getBool("PrecisionDriving") == true && precision == false){
            driver.downScale(0.5f);
            precision = true;
        }
        else if (input.getBool("PrecisionDriving") == true && precision == true){
            precision = true;
        }
        else if (input.getBool("PrecisionDriving") == false && precision == true){
            driver.upScale(0.5f);
            precision = false;
        }
        else {
            precision = false;
        }

        telemetry.update();
    }

    public void stop() {
        FeatureManager.setIsOpModeRunning(false);
    }
}
