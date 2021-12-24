package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.macro.Macro;

public abstract class AutoautoMacro extends Macro {
    public AutoautoProgram program;

    private AutoautoRuntime runtime = null;
    private boolean forceStop;

    public abstract AutoautoProgram generateProgram();

    @Override
    public final void start(FeatureManager... managers) {
        program = generateProgram();
        if(runtime == null) runtime = new AutoautoRuntime(program, getClass().getCanonicalName(), managers);
        else runtime.setProgram(program, getClass().getCanonicalName());
    }

    @Override
    public final void loop() {
        runtime.loop();
    }

    @Override
    public final void stop() {
    }
}
