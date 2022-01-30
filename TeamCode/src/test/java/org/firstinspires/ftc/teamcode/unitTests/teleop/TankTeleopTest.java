package org.firstinspires.ftc.teamcode.unitTests.teleop;

import org.firstinspires.ftc.teamcode.opmodes.teleop.TankTeleop;
import org.firstinspires.ftc.teamcode.unitTests.opmodetesting.OpmodeTester;
import org.junit.Test;

public class TankTeleopTest {
    @Test
    public void test() {
        OpmodeTester.runTestOn(new TankTeleop());
    }
}
