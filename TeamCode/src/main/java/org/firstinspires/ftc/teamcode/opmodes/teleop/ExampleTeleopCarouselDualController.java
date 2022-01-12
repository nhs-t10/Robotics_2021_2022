package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager.crservo;
import static org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager.motor;
import static org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager.servo;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.auxilary.integratedasync.PriorityAsyncOpmodeComponent;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputOverlapResolutionMethod;
import org.firstinspires.ftc.teamcode.managers.input.nodes.BothNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.ButtonNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.EitherNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.JoystickNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.MultiInputNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.ScaleNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.StaticValueNode;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
import org.firstinspires.ftc.teamcode.managers.nate.NateManager;
import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.junit.Test;

import java.util.Arrays;

@TeleOp
public class ExampleTeleopCarouselDualController extends OpMode {
    public MovementManager driver;
    public ManipulationManager hands;
    public InputManager input;
    public NateManager clawPosition;
    private boolean precision = false;
    private boolean dashing = false;

    @Override
    public void init() {
        // Phone is labelled as Not Ready For Use
        FeatureManager.setIsOpModeRunning(true);
        TelemetryManager telemetryManager = new TelemetryManager(telemetry, this, TelemetryManager.BITMASKS.NONE);
        telemetry = telemetryManager;

        FeatureManager.logger.setBackend(telemetry.log());
        FeatureManager.setIsOpModeRunning(true);

        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr");
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        driver = new MovementManager(fl, fr, br, bl);
        hands = new ManipulationManager(
                hardwareMap,
                crservo         ("nateMoverRight", "nateMoverLeft"),
                servo           ("nateClaw", "rampLeft", "rampRight"),
                motor           ("Carousel", "ClawMotor", "noodle", "intake")
        );

        clawPosition = new NateManager(hands, hardwareMap.get(TouchSensor.class, "limit"));
        input = new InputManager(gamepad1, gamepad2);
        input.registerInput("drivingControls",
                new MultiInputNode(
                        new JoystickNode("left_stick_y"),
                        new JoystickNode("right_stick_x"),
                        new JoystickNode("left_stick_x")
                )
        );
        input.registerInput("drivingControlsMicro",
                new ScaleNode(new MultiInputNode(
                        new JoystickNode("gamepad2left_stick_y"),
                        new JoystickNode("gamepad2right_stick_x"),
                        new JoystickNode("gamepad2left_stick_x")
                ), 0.4f)
        );
        input.setOverlapResolutionMethod(InputOverlapResolutionMethod.MOST_COMPLEX_ARE_THE_FAVOURITE_CHILD);
        input.registerInput("precisionDriving", new ButtonNode("b"));
        input.registerInput("dashing", new ButtonNode("x"));
        input.registerInput("CarouselBlue", new ButtonNode("y"));
        input.registerInput("CarouselRed", new ButtonNode("a"));
        input.registerInput("ClawPos1", new ButtonNode ("gamepad2y"));
        input.registerInput("ClawPos2", new ButtonNode ("gamepad2b"));
        input.registerInput("ClawPos3", new ButtonNode ("gamepad2a"));
        input.registerInput("ClawPosHome", new ButtonNode("gamepad2x"));
        input.registerInput("ClawUp", new ButtonNode("gamepad2dpadup"));
        input.registerInput("ClawDown", new ButtonNode("gamepad2dpaddown"));
        input.registerInput("ToggleClaw", new ButtonNode("gamepad2leftbumper"));
        input.registerInput("turnAround", new StaticValueNode(0)); //chloe note: merging did weird stuff & this showed up oddly.
        input.registerInput("Intake",
                new EitherNode(
                        new ButtonNode("righttrigger"),
                        new ButtonNode("gamepad2righttrigger")
                ));
        input.registerInput("Anti-Intake",
                new EitherNode(
                        new ButtonNode("lefttrigger"),
                        new ButtonNode("gamepad2lefttrigger")
                ));
        input.registerInput("EmergencyStop",
                new EitherNode(
                        new EitherNode(
                                new ButtonNode("dpadleft"),
                                new ButtonNode("gamepad2dpadleft")
                        ),
                        new EitherNode(
                                new ButtonNode("dpadright"),
                                new ButtonNode("gamepad2dpadright")
                        )
                ));
        input.registerInput("Failsafe",
                new EitherNode(
                        new ButtonNode("start"),
                        new ButtonNode("gamepad2start")
                ));
        hands.setMotorMode("ClawMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hands.setMotorMode("ClawMotor", DcMotor.RunMode.RUN_USING_ENCODER);


        PriorityAsyncOpmodeComponent.start(() -> {
            driver.driveOmni(input.getFloatArrayOfInput("drivingControls")[0]+input.getFloatArrayOfInput("drivingControlsMicro")[0],
                            input.getFloatArrayOfInput("drivingControls")[1]+input.getFloatArrayOfInput("drivingControlsMicro")[1],
                            input.getFloatArrayOfInput("drivingControls")[2]+input.getFloatArrayOfInput("drivingControlsMicro")[2]);
        });


    }

    @Override
    public void loop() {
        input.update();

        if (input.getBool("precisionDriving") == true && precision == false) {
            driver.downScale(0.5f);
            precision = true;
        } else if (input.getBool("precisionDriving") == true && precision == true) {
            precision = true;
        } else if (input.getBool("precisionDriving") == false && precision == true) {
            driver.upScale(0.5f);
            precision = false;
        } else {
            precision = false;
        }

        if (input.getBool("dashing") == true && dashing == false) {
            driver.upScale(0.4f);
            dashing = true;
        } else if (input.getBool("precisionDriving") == true && dashing == true) {
            dashing = true;
        } else if (input.getBool("precisionDriving") == false && dashing == true) {
            driver.downScale(0.4f);
            dashing = false;
        } else {
            dashing = false;
        }
        if (input.getBool("Failsafe") == false){
            if (input.getBool("Intake")){
                hands.setMotorPower("noodle", 0.9);
                hands.setMotorPower("intake", 1);
                hands.setServoPosition("rampLeft", 0.50);
                hands.setServoPosition("rampRight", 0.0);
            }
            else if (input.getBool("Anti-Intake")){
                hands.setMotorPower("noodle", -0.50);
                hands.setMotorPower("intake", -0.50);
                hands.setServoPosition("rampLeft", 0.50);
                hands.setServoPosition("rampRight", 0.0);
            }
            else {
                hands.setMotorPower("noodle", 0.0);
                hands.setMotorPower("intake", 0.0);
                hands.setServoPosition("rampLeft", 0.0);
                hands.setServoPosition("rampRight", 0.35);
            }
            if (input.getBool("EmergencyStop")){
                clawPosition.emergencyStop();
            }
            if (input.getBool("CarouselBlue") && input.getBool("CarouselRed") == false){
                hands.setMotorPower("Carousel", 0.75);
            }
            else if (input.getBool("CarouselRed") && input.getBool("CarouselBlue") == false) {
                hands.setMotorPower("Carousel", -0.75);
            }
            else {
                hands.setMotorPower("Carousel", 0.0);
            }

            if (hands.hasEncodedMovement("ClawMotor") == false) {
                if (input.getBool("ClawUp") == true && input.getBool("ClawDown") == false) {
                    hands.setMotorMode("ClawMotor", DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    hands.setMotorPower("ClawMotor", -0.25);
                }
                if (input.getBool("ClawDown") == true && input.getBool("ClawUp") == false) {
                    hands.setMotorMode("ClawMotor", DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    hands.setMotorPower("ClawMotor", 0.25);
                }
                if (input.getBool("ClawUp") == false && input.getBool("ClawDown") == false) {
                    hands.setMotorMode("ClawMotor", DcMotor.RunMode.RUN_USING_ENCODER);
                    hands.setMotorPower("ClawMotor", 0.0);
                }
            }
            if (input.getBool("ToggleClaw") == true){
                clawPosition.setClawOpen(true);
            }
            else {
                clawPosition.setClawOpen(false);
            }
            if (input.getBool("ClawPos1") == true) {
                clawPosition.positionOne();
            }
            if (input.getBool("ClawPos2") == true) {
                clawPosition.positionTwo();
            }
            if (input.getBool("ClawPos3") == true) {
                clawPosition.positionThree();
            }
            if (input.getBool("ClawPosHome") == true) {
                clawPosition.positionHome();
            }
        }
        telemetry.addData("FL Power", driver.frontLeft.getPower());
        telemetry.addData("FR Power", driver.frontRight.getPower());
        telemetry.addData("BR Power", driver.backLeft.getPower());
        telemetry.addData("BL Power", driver.backRight.getPower());
        telemetry.addData("Pos Y (encoders)", driver.getCentimeters());
        telemetry.addData("WhichBoy", FeatureManager.getRobotName());
        telemetry.addData("Claw Open Position", clawPosition.getClawOpenish());
        telemetry.addData("Carousel", hands.getMotorPower("Carousel"));
        telemetry.addData("driver control", Arrays.toString(input.getFloatArrayOfInput("drivingControls")));
        telemetry.addData("ClawTowerTicks", hands.getMotorPosition("ClawMotor"));
        telemetry.addData("ClawTowerTarTicks", hands.getMotorTargetPosition("ClawMotor"));
        telemetry.addData("ClawTowerPower", hands.getMotorPower("ClawMotor"));
        telemetry.addData("Is Found", clawPosition.isFound());
        telemetry.update();
    }

    public void stop() {
        FeatureManager.setIsOpModeRunning(false);
    }

    @Test
    public void test() {
        this.hardwareMap = new DummyHardwareMap();
        this.gamepad1 = new DummyGamepad();
        this.gamepad2 = new DummyGamepad();
        this.telemetry = new DummyTelemetry();

        long startTime = System.currentTimeMillis();

        this.start();
        this.init();
        this.init_loop();
        for(int i = 0; i < 100; i++) {
            this.time = (System.currentTimeMillis() - startTime) * 0.001;
            this.loop();
        }
        this.stop();
    }
}
