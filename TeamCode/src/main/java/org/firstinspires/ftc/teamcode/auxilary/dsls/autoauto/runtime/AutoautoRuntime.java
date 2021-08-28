package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.managers.ImuManager;
import org.firstinspires.ftc.teamcode.managers.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.MovementManager;
import org.firstinspires.ftc.teamcode.managers.SensorManager;

public class AutoautoRuntime {
    public AutoautoRuntimeVariableScope globalScope;
    public AutoautoProgram program;

    public static AutoautoRuntime R(AutoautoProgram program, MovementManager drive, ManipulationManager manip, SensorManager sense, ImuManager imu) {
        return new AutoautoRuntime(program, drive, manip, sense, imu);
    }

    public AutoautoRuntime(AutoautoProgram program, MovementManager drive, ManipulationManager manip, SensorManager sense, ImuManager imu) {
        this.globalScope = new AutoautoRuntimeVariableScope();
        globalScope.initSugarVariables();
        globalScope.initBuiltinFunctions(this);

        this.program = program;
        this.program.setScope(globalScope);

        NativeRobotFunction[] builtInFunctions = RobotFunctionLoader.loadFunctions(drive, manip, sense, imu);
        RobotFunctionLoader.addFunctionsToStore(builtInFunctions, globalScope);

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
