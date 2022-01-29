package org.firstinspires.ftc.teamcode.unitTests.teleop;

import org.firstinspires.ftc.teamcode.opmodes.teleop.DualController;
import org.firstinspires.ftc.teamcode.opmodes.teleop.SingleController;
import org.firstinspires.ftc.teamcode.unitTests.OpmodeTester;
import org.junit.Test;

public class DualControllerTest {
    @Test
    public void test() {
        OpmodeTester.runTestOn(new DualController());
    }
}
