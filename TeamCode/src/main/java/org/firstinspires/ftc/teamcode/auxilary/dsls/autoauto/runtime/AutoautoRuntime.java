package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.input.InputManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

public class AutoautoRuntime {
    public AutoautoRuntimeVariableScope globalScope;
    public AutoautoProgram program;

    public static AutoautoRuntime R(AutoautoProgram program, MovementManager drive, ManipulationManager manip, SensorManager sense, TelemetryManager tel) {
        return new AutoautoRuntime(program, drive, manip, sense, tel);
    }

    public AutoautoRuntime(AutoautoProgram program, MovementManager drive, ManipulationManager manip, SensorManager sense, TelemetryManager telemetry) {
        this.globalScope = new AutoautoRuntimeVariableScope();
        globalScope.initSugarVariables();
        globalScope.initBuiltinFunctions(this);

        this.program = program;
        this.program.setScope(globalScope);

        InputManager dummyInputMan = new InputManager(null, null);

        RobotFunctionLoader.loadFunctions(new FeatureManager(), dummyInputMan, manip, drive, sense, telemetry, globalScope);

        ManagerDeviceScanner.scan(globalScope, manip, sense);

        this.program.init();
        this.program.stepInit();
    }

    public int getCurrentState() {
        return (int)((AutoautoNumericValue)globalScope.get(AutoautoSystemVariableNames.STATE_NUMBER)).getFloat();
    }

    public String getCurrentStatepath() {
        return ((AutoautoString)(globalScope.get(AutoautoSystemVariableNames.STATEPATH_NAME))).getString();
    }

    public void loop() {
        this.program.loop();
    }


}
