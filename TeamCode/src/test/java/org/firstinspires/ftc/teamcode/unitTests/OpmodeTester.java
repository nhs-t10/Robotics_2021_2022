package org.firstinspires.ftc.teamcode.unitTests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.teamcode.auxilary.RobotTime;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.opmodes.teleop.DualControllerFools;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOutputStream;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;

import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class OpmodeTester {
    public static void runTestOn(OpMode opmode) {
        runTestOn(opmode, 300_000);
    }
    public static void runTestOn(OpMode opmode, long simulatedMsFor) {
        runTestOn(opmode, simulatedMsFor, 1000);
    }
    public static void runTestOn(OpMode opmode, long simulatedMsFor, long realMsFor) {
        PrintStream originalStdout = FeatureManager.logger.fallbackBackend;
        FeatureManager.logger.setBackend(new PrintStream(new DummyOutputStream()));

        Clock originalClock = RobotTime.clock;
        RobotTime.clock = new TimeDilatedClock(simulatedMsFor / realMsFor);

        opmode.hardwareMap = new DummyHardwareMap();
        opmode.gamepad1 = new DummyGamepad();
        opmode.gamepad2 = new DummyGamepad();
        opmode.telemetry = new DummyTelemetry();

        long startRobotTime = RobotTime.currentTimeMillis();
        long startRealTime = System.currentTimeMillis();

        opmode.start();
        opmode.init();
        opmode.init_loop();
        while(System.currentTimeMillis() - startRealTime < realMsFor) {
            opmode.time = RobotTime.currentTimeMillis() - startRobotTime;
            opmode.loop();
        }
        opmode.stop();
        RobotTime.clock = originalClock;
        FeatureManager.logger.setBackend(originalStdout);
    }

    private static class TimeDilatedClock extends Clock {
        private final ZoneId zone = ZoneId.systemDefault();
        private long timeDilation = 0;

        private final long startTime;

        public TimeDilatedClock(long td) {
            this.timeDilation = td;
            this.startTime = System.currentTimeMillis();
        }

        @Override
        public ZoneId getZone() {
            return zone;
        }

        @Override
        public Clock withZone(ZoneId zoneId) {
            return this;
        }

        @Override
        public Instant instant() {
            long delta = System.currentTimeMillis() - startTime;
            return Instant.ofEpochMilli(startTime + delta * timeDilation);
        }

        protected void setTimeDilationFactor(long coef) {
            this.timeDilation = coef;
        }
    }
}
