package org.firstinspires.ftc.teamcode.unitTests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.opmodes.teleop.DualControllerFools;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;

public class OpmodeTester {
    public static void runTestOn(OpMode opmode) {
        opmode.hardwareMap = new DummyHardwareMap();
        opmode.gamepad1 = new DummyGamepad();
        opmode.gamepad2 = new DummyGamepad();
        opmode.telemetry = new DummyTelemetry();

        long startTime = System.currentTimeMillis();

        opmode.start();
        opmode.init();
        opmode.init_loop();
        for(int i = 0; i < 100; i++) {
            opmode.time = (System.currentTimeMillis() - startTime) * 0.001;
            opmode.loop();
        }
        opmode.stop();
    }
}
