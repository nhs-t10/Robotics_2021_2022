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
    private final FeatureManager[] managers;
    public AutoautoRuntimeVariableScope globalScope;
    public AutoautoProgram program;

    public static AutoautoRuntime R(AutoautoProgram program, FeatureManager... managers) {

        try {
            return new AutoautoRuntime(program, managers);
        } catch (ManagerSetupException e) {
            FeatureManager.logger.log(e.toString());
            return null;
        }
    }

    public AutoautoRuntime(AutoautoProgram program, FeatureManager... managers) {
        MovementManager drive = null;
        ManipulationManager manip = null;
        SensorManager sense = null;
        TelemetryManager telemetry = null;
        //find managers of each required type
        for(FeatureManager f : managers) {
            if(f instanceof MovementManager) drive = (MovementManager) f;
            if(f instanceof ManipulationManager) manip = (ManipulationManager) f;
            if(f instanceof SensorManager) sense = (SensorManager) f;
            if(f instanceof TelemetryManager) telemetry = (TelemetryManager) f;
        }

        if(drive == null || manip == null || sense == null || telemetry == null) throw new ManagerSetupException("Managers must contain at least one of each type.");
        this.managers = managers;
        setProgram(program);
    }

    public int getCurrentState() {
        return (int)((AutoautoNumericValue)globalScope.get(AutoautoSystemVariableNames.STATE_NUMBER)).getFloat();
    }

    public String getCurrentStatepath() {
        return globalScope.get(AutoautoSystemVariableNames.STATEPATH_NAME).getString();
    }

    public void loop() {
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
    }
}
