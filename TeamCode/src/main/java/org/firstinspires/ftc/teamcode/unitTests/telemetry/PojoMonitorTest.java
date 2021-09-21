package org.firstinspires.ftc.teamcode.unitTests.telemetry;

import org.firstinspires.ftc.teamcode.managers.telemetry.pojotracker.OhNoJavaFieldMonitorAndExposer;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOpmode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PojoMonitorTest {
    @Test
    public void test() {
        OhNoJavaFieldMonitorAndExposer monitor = new OhNoJavaFieldMonitorAndExposer(new TestPojoOpmode());
        assertEquals("{\"testString\":\"foobar\",\"testInt\":\"3\",\"testFloat\":\"2.4\",\"time\":\"0.0\",\"msStuckDetectInit\":\"5000\",\"msStuckDetectInitLoop\":\"5000\",\"msStuckDetectStart\":\"5000\",\"msStuckDetectLoop\":\"5000\",\"msStuckDetectStop\":\"900\"}", monitor.getJSON());
    }

    public static class TestPojoOpmode extends DummyOpmode {
        public String testString = "foobar";
        public int testInt = 3;
        public float testFloat = 2.4f;

        private String thisOneShouldntBePrinted;
        public String nullField;

        public String thisOnesAMethod() {
            return "";
        }
    }
}
