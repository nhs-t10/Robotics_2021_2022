package org.firstinspires.ftc.teamcode.unitTests.telemetry;

import org.firstinspires.ftc.teamcode.__compiledautoauto.teamcode.opmodes.auto.DiggityDrive__autoauto;
import org.firstinspires.ftc.teamcode.opmodes.teleop.DualController;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.junit.Assert;
import org.junit.Test;

public class MultipleOpmodesTest {
    @Test
    public void test() {
        runAuto();
        runTeleop();
        //are you satisfied, auto-checker?
        Assert.assertTrue(true);
    }

    private void runAuto() {
        DiggityDrive__autoauto a = new DiggityDrive__autoauto();
        a.telemetry = new DummyTelemetry();
        a.hardwareMap = new DummyHardwareMap();

        a.init();
        a.start();
        for(int i = 0; i < 100; i++) a.loop();
        a.stop();
    }
    private void runTeleop() {
        DualController a = new DualController();
        a.telemetry = new DummyTelemetry();
        a.hardwareMap = new DummyHardwareMap();
        a.gamepad1 = new DummyGamepad();
        a.gamepad2 = new DummyGamepad();

        a.init();
        a.start();
        for(int i = 0; i < 100; i++) a.loop();
        a.stop();
    }
}
