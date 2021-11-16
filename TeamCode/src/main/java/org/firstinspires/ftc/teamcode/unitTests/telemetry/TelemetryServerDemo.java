package org.firstinspires.ftc.teamcode.unitTests.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOpmode;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.junit.Test;

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

        DummyOpmode o = new DummyOpmode();
        TelemetryManager telemetry = new TelemetryManager(new DummyTelemetry(), o, TelemetryManager.BITMASKS.ALL);

        o.hardwareMap.tryGet(DcMotor.class, "foo");
        o.hardwareMap.tryGet(DcMotor.class, "bar");

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
