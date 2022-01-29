package org.firstinspires.ftc.teamcode.unitTests.teleop;

import org.firstinspires.ftc.teamcode.opmodes.teleop.TeleopStripped;

import org.firstinspires.ftc.teamcode.unitTests.OpmodeTester;
import org.junit.Test;

public class TeleopStrippedTest {
    @Test
    public void test() {
        OpmodeTester.runTestOn(new TeleopStripped());
    }
}
