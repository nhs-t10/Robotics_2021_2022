package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.encapsulation;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerDeviceScanner;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.RobotFunctionLoader;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class AutoautoModule {
    public AutoautoRuntimeVariableScope globalScope;
    public AutoautoProgram program;
    public String address;

    public AutoautoModule(AutoautoProgram p, String address, FeatureManager[] managers) {
        this.address = address;
        this.program = p;

        if(program == null) throw new IllegalStateException("Program may not be null");

        this.globalScope = new AutoautoRuntimeVariableScope();
        globalScope.initSugarVariables();
        globalScope.initBuiltinFunctions(this, managers);

        this.program.setScope(globalScope);

        RobotFunctionLoader.loadFunctions(globalScope, managers);

        ManagerDeviceScanner.scan(globalScope, managers);

        this.program.init();
        this.program.stepInit();
    }

    public void loop() {
        this.program.loop();
    }

    public AutoautoPrimitive getExports() {
        AutoautoPrimitive p = globalScope.get(AutoautoSystemVariableNames.EXPORTS);
        if(p == null) return new AutoautoUndefined();
        else return p;
    }
}
