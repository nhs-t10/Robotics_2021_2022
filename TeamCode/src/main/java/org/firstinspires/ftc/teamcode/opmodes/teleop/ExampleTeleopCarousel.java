package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.input.InputOverlapResolutionMethod;
import org.firstinspires.ftc.teamcode.managers.input.nodes.BothNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.ButtonNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.ComboNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.JoystickNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.MultiInputNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.ScaleNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.TimeNode;
import org.firstinspires.ftc.teamcode.managers.macro.MacroManager;
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

import static org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager.crservo;
import static org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager.motor;
import static org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager.servo;

@TeleOp
public class ExampleTeleopCarousel extends OpMode {
    private MovementManager driver;
    private ManipulationManager hands;
    private InputManager input;
    private SensorManager sensor;
    private ImuManager imu;
    private MacroManager macroManager;
    private NateManager clawPosition;
    private boolean precision = false;
    private boolean dashing = false;

    public DcMotor NewMotor(DcMotor motor, String name) {
        motor = hardwareMap.get(DcMotor.class, name);
        return motor;
    }

    @Override
    public void init() {
        // Phone is labelled as Not Ready For Use
        FeatureManager.setIsOpModeRunning(true);
        TelemetryManager telemetryManager = new TelemetryManager(telemetry, this, TelemetryManager.BITMASKS.WEBSERVER | TelemetryManager.BITMASKS.FALLIBLE_HARDWARE);
        telemetry = telemetryManager;

//        imu = new ImuManager(hardwareMap.get(com.qualcomm.hardware.bosch.BNO055IMU.class, "imu"));

        DcMotor fl = hardwareMap.get(DcMotor.class, "fl");
        DcMotor fr = hardwareMap.get(DcMotor.class, "fr");
        DcMotor br = hardwareMap.get(DcMotor.class, "br");
        DcMotor bl = hardwareMap.get(DcMotor.class, "bl");
        driver = new MovementManager(fl, fr, br, bl);
        hands = new ManipulationManager(
                hardwareMap,
                crservo         ("nateMoverLeft", "nateMoverRight"),
                servo           ("nateClaw", "rampLeft", "rampRight", "intakeMoverRight", "intakeMoverLeft"),
                motor           ("Carousel", "ClawMotor", "noodle", "intake")
        );
        clawPosition = new NateManager(hands);
        input = new InputManager(gamepad1, gamepad2);
        input.registerInput("drivingControls",
                new MultiInputNode(
                        new JoystickNode("left_stick_y"),
                        new JoystickNode("left_stick_x"),
                        new JoystickNode("right_stick_x")
                )
        );
        input.registerInput("precisionDriving", new ButtonNode("b"));
        input.registerInput("dashing", new ButtonNode("x"));
        input.registerInput("Carousel", new ButtonNode("y"));
        input.registerInput("EmergencyStop", new ButtonNode("select"));
        input.registerInput("ClawPos1",
                new BothNode(
                        new ButtonNode("leftbumper"),
                        new ButtonNode ("x")
                    ));
        input.registerInput("ClawPos2",
                new BothNode(
                        new ButtonNode("leftbumper"),
                        new ButtonNode ("y")
                ));
        input.registerInput("ClawPos3",
                new BothNode(
                        new ButtonNode("leftbumper"),
                        new ButtonNode ("b")
                ));
        input.registerInput("ClawPosHome",
                new BothNode(
                        new ButtonNode("leftbumper"),
                        new ButtonNode("a")
                ));
        input.registerInput("ClawShiftIn",
                    new BothNode(
                        new ButtonNode("rightbumper"),
                        new ButtonNode("a")
                    ));
        input.registerInput("ClawShiftOut",
                new BothNode(
                        new ButtonNode("rightbumper"),
                        new ButtonNode("b")
                ));
        input.setOverlapResolutionMethod(InputOverlapResolutionMethod.MOST_COMPLEX_ARE_THE_FAVOURITE_CHILD);
        input.registerInput("ToggleClaw", new ButtonNode("lefttrigger"));
        input.registerInput("ClawUp", new ButtonNode("dpadup"));
        input.registerInput("ClawDown", new ButtonNode("dpaddown"));
        input.registerInput("turnAround", new ButtonNode("right_stick_button"));
        input.registerInput("Intake", new ButtonNode("righttrigger"));
        hands.setMotorMode("ClawMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hands.setMotorMode("ClawMotor", DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void loop() {
        input.update();
        float[] drive = input.getFloatArrayOfInput("drivingControls");
        driver.driveOmni(drive[0], drive[1], drive[2]);

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

        if (input.getBool("Intake")){
            hands.setMotorPower("noodle", 1);
            hands.setMotorPower("intake", 1);
            hands.setServoPosition("rampLeft", 0.5);
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
        hands.setMotorPower("Carousel", input.getFloat("Carousel") * 0.5);
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
            clawPosition.toggleClaw();
        }
        if (input.getBool("ClawShiftIn") == true){
            hands.setServoPower("nateMoverLeft", 1.0);
            hands.setServoPower("nateMoverRight", -1.0);
        }
        if (input.getBool("ClawShiftOut") == true){
            hands.setServoPower("nateMoverRight", 1.0);
            hands.setServoPower("nateMoverLeft", -1.0);
        }
        if (input.getBool("ClawShiftIn") == false && input.getBool("ClawShiftOut") == false){
            hands.setServoPower("nateMoverRight", 0.0);
            hands.setServoPower("nateMoverLeft", 0.0);
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
        telemetry.addData("FL Power", driver.frontLeft.getPower());
        telemetry.addData("FR Power", driver.frontRight.getPower());
        telemetry.addData("BR Power", driver.backLeft.getPower());
        telemetry.addData("BL Power", driver.backRight.getPower());
        telemetry.addData("WhichBoy", FeatureManager.getRobotName());
        telemetry.addData("Carousel", hands.getMotorPower("Carousel"));
        telemetry.addData("driver control", Arrays.toString(input.getFloatArrayOfInput("drivingControls")));
        telemetry.addData("ClawTowerTicks", hands.getMotorPosition("ClawMotor"));
        telemetry.addData("ClawTowerTarTicks", hands.getMotorTargetPosition("ClawMotor"));
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
