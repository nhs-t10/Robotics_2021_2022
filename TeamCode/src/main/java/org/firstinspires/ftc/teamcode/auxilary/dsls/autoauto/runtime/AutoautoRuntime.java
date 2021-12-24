package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.encapsulation.AutoautoModule;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.util.HashMap;
import java.util.Stack;

public class AutoautoRuntime {
    private final FeatureManager[] managers;

    public AutoautoModule rootModule;

    public static AutoautoRuntime R(AutoautoProgram program, String creatorAddress, FeatureManager... managers) {
        return new AutoautoRuntime(program, creatorAddress, managers);
    }

    public AutoautoRuntime(AutoautoProgram program, String creatorAddress, FeatureManager... managers) {
        this.managers = managers;

        setProgram(program, creatorAddress);
    }
    public AutoautoRuntime(FeatureManager... managers) {
        this.managers = managers;
    }

    public int getCurrentState() {
        return (int)((AutoautoNumericValue)rootModule.globalScope.get(AutoautoSystemVariableNames.STATE_NUMBER)).getFloat();
    }

    public String getCurrentStatepath() {
        return rootModule.globalScope.get(AutoautoSystemVariableNames.STATEPATH_NAME).getString();
    }

    public void loop() {
        rootModule.loop();
    }

    public void setProgram(AutoautoProgram program, String creatorAddress) {
        rootModule = new AutoautoModule(program, creatorAddress, managers);
    }
}
