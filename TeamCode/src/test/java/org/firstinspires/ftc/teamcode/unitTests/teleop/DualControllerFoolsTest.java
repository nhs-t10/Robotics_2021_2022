package org.firstinspires.ftc.teamcode.unitTests.teleop;

import org.firstinspires.ftc.teamcode.opmodes.teleop.DualControllerFools;
import org.firstinspires.ftc.teamcode.unitTests.opmodetesting.OpmodeTester;
import org.junit.Test;

public class DualControllerFoolsTest {
    @Test
    public void test() {
        OpmodeTester.runTestOn(new DualControllerFools());
    }
}
