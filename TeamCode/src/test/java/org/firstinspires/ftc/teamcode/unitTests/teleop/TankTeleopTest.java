package org.firstinspires.ftc.teamcode.unitTests.teleop;

import org.firstinspires.ftc.teamcode.opmodes.teleop.DualControllerFools;
import org.firstinspires.ftc.teamcode.opmodes.teleop.TankTeleop;
import org.firstinspires.ftc.teamcode.unitTests.opmodetesting.OpmodeTester;
import org.junit.Test;

import static org.firstinspires.ftc.robotcore.internal.system.Assert.assertTrue;

public class TankTeleopTest {
    @Test
    public void test() {
        assertTrue(OpmodeTester.runTestOn(new TankTeleop()));
    }
}
