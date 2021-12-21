package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

public class AutoautoRuntime {
    private final FeatureManager[] managers;
    public AutoautoRuntimeVariableScope globalScope;
    public AutoautoProgram program;
    private boolean calledInit;

    public static AutoautoRuntime R(AutoautoProgram program, FeatureManager... managers) {
        return new AutoautoRuntime(program, managers);
    }

    public AutoautoRuntime(AutoautoProgram program, FeatureManager... managers) {
        this.managers = managers;
        setProgram(program);
    }
    public AutoautoRuntime(FeatureManager... managers) {
        this.managers = managers;
    }

    public int getCurrentState() {
        return (int)((AutoautoNumericValue)globalScope.get(AutoautoSystemVariableNames.STATE_NUMBER)).getFloat();
    }

    public String getCurrentStatepath() {
        return globalScope.get(AutoautoSystemVariableNames.STATEPATH_NAME).getString();
    }

    public void loop() {
        assert calledInit == true;
        this.program.loop();
    }

    public void setProgram(AutoautoProgram program) {
        if(program == null) throw new IllegalStateException("Program may not be null");

        this.globalScope = new AutoautoRuntimeVariableScope();
        globalScope.initSugarVariables();
        globalScope.initBuiltinFunctions(this);

        this.program = program;
        this.program.setScope(globalScope);

        RobotFunctionLoader.loadFunctions(globalScope, managers);

        ManagerDeviceScanner.scan(globalScope, managers);

        this.program.init();
        this.program.stepInit();

        this.calledInit = true;
    }
}
