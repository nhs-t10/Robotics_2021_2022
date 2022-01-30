package org.firstinspires.ftc.teamcode.unitTests.opmodetesting;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auxilary.RobotTime;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.unitTests.TestTypeManager;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOutputStream;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;

import java.io.PrintStream;
import java.time.Clock;

public class OpmodeTester {
    static RunWhileInLoopFunction DEFAULT_INLOOP_FUNCTION = new DoNothingWhileInLoopFunction();
    static final long DEFAULT_SIMULATED_MS = TestTypeManager.testRunTypeIs("long") ? 300_000 : 30_000;
    static final long DEFAULT_REALTIME_MS = TestTypeManager.testRunTypeIs("long") ? 1000 : 100;

    public static void runTestOn(OpMode opmode) {
        runTestOn(opmode, DEFAULT_SIMULATED_MS);
    }
    public static void runTestOn(OpMode opmode, long simulatedMsFor) {
        runTestOn(opmode, simulatedMsFor, DEFAULT_INLOOP_FUNCTION);
    }
    public static void runTestOn(OpMode opmode, long simulatedMsFor, RunWhileInLoopFunction function) {
        runTestOn(opmode, simulatedMsFor, function, DEFAULT_REALTIME_MS);
    }
    public static void runTestOn(OpMode opmode, long simulatedMsFor, RunWhileInLoopFunction function, long realMsFor) {
        PrintStream originalStdout = FeatureManager.logger.fallbackBackend;
        DummyOutputStream fakeStdout = new DummyOutputStream();
        FeatureManager.logger.setBackend(new PrintStream(fakeStdout));

        Clock originalClock = RobotTime.clock;
        RobotTime.clock = new TimeDilatedClock(simulatedMsFor / realMsFor);

        opmode.hardwareMap = new DummyHardwareMap();
        opmode.gamepad1 = new DummyGamepad();
        opmode.gamepad2 = new DummyGamepad();
        opmode.telemetry = new DummyTelemetry();

        long startRealTime = System.currentTimeMillis();

        OpmodeLoopRunnerThread runner = new OpmodeLoopRunnerThread(opmode);
        runner.start();

        while(System.currentTimeMillis() - startRealTime < realMsFor) {
            function.looper(RobotTime.currentTimeMillis());
        }
        runner.testOver();

        RobotTime.clock = originalClock;
        FeatureManager.logger.setBackend(originalStdout);
        FeatureManager.logger.log(fakeStdout.getBuf());
    }

}
