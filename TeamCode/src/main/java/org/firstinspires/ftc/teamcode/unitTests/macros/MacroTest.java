package org.firstinspires.ftc.teamcode.unitTests.macros;



import org.firstinspires.ftc.teamcode.__compiledautoauto.teamcodeunittestsmacros$vP5M1.Testmacro__macro_autoauto;
import org.firstinspires.ftc.teamcode.auxilary.Sensor;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.macro.MacroManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyDcMotor;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyImu;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOpmode;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.junit.Test;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MacroTest {
    @Test
    public void test() {
        FeatureManager.setIsOpModeRunning(true);

        DummyOpmode opmode = new DummyOpmode();

        FeatureManager.logger.setBackend(opmode.telemetry.log());

        SensorManager sensor = new SensorManager(new Sensor[] {}, new String[] {});
        MovementManager driver = new MovementManager(new DummyDcMotor(), new DummyDcMotor(), new DummyDcMotor(), new DummyDcMotor());
        TelemetryManager telemetry = new TelemetryManager(opmode);
        ManipulationManager hands = new ManipulationManager(new DummyHardwareMap(), new String[] {}, new String[] {}, new String[] {});
        ImuManager imu = new ImuManager(new DummyImu());
        InputManager input = new InputManager(new DummyGamepad(), new DummyGamepad());

        MacroManager macroManager = new MacroManager(sensor, driver, telemetry, hands, imu, input);


        macroManager.registerMacro("test", new Testmacro__macro_autoauto());

        macroManager.runMacro("test");

        long startTime = System.currentTimeMillis();

        while(macroManager.isMacroRunning()) {
            if(System.currentTimeMillis() - 1000 > startTime) {
                fail();
            }
        }

        FeatureManager.setIsOpModeRunning(false);

        String logOutput = ((DummyTelemetry.DummyLog)opmode.telemetry.log()).getLogText();
        assertThat("Log printed correctly", logOutput, containsString("First state\nSecond state\n"));
    }
}
