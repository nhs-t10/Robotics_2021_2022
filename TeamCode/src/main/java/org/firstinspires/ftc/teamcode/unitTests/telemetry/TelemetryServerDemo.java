package org.firstinspires.ftc.teamcode.unitTests.telemetry;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.ControlCodes;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.Headers;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOpmode;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TelemetryServerDemo {
    @Test
    public void test() {
        //don't run this when it's under all tests
        try {
            if (System.getenv("TEST_TYPE").equalsIgnoreCase("ALL")) {
                FeatureManager.logger.log("Not running demo; switch to individual configuration for that.");
                return;
            }
        } catch(Throwable onNonPortableProblemIgnore) {
            //ignore the problem
        }
        long TEST_TIME_MS = 1_200_000;

        long start = System.currentTimeMillis();

        FeatureManager.setIsOpModeRunning(true);

        TelemetryManager telemetry = new TelemetryManager(new DummyTelemetry(), new DummyOpmode(), TelemetryManager.BITMASKS.WEBSERVER);

        while(System.currentTimeMillis() - start < TEST_TIME_MS) {
            telemetry.addData("foo", Math.random() * 3);

            telemetry.addData("fl", Math.random() * 2 - 1);
            telemetry.addData("bl", Math.random() * 2 - 1);
            telemetry.addData("fr", Math.random() * 2 - 1);
            telemetry.addData("br", Math.random() * 2 - 1);

            telemetry.addData("str", "fe");

            telemetry.update();
        }


        FeatureManager.setIsOpModeRunning(false);
    }
}
