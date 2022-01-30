package org.firstinspires.ftc.teamcode.unitTests.teleop;

import org.firstinspires.ftc.teamcode.opmodes.teleop.DualControllerPreston;
import org.firstinspires.ftc.teamcode.unitTests.opmodetesting.OpmodeTester;
import org.junit.Test;

public class DualControllerPrestonTest {
    @Test
    public void test() {
        OpmodeTester.runTestOn(new DualControllerPreston());
    }
}
