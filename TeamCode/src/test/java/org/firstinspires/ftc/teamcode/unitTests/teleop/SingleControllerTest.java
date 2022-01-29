package org.firstinspires.ftc.teamcode.unitTests.teleop;

import org.firstinspires.ftc.teamcode.opmodes.teleop.SingleController;
import org.firstinspires.ftc.teamcode.unitTests.OpmodeTester;
import org.junit.Test;

public class SingleControllerTest {
    @Test
    public void test() {
        OpmodeTester.runTestOn(new SingleController());
    }
}
