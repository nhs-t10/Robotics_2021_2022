package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.macro.Macro;

public abstract class AutoautoMacro extends Macro {
    public AutoautoProgram program;

    private AutoautoRuntime runtime = null;
    private boolean forceStop;
    private boolean started;

    public abstract AutoautoProgram generateProgram();

    @Override
    public final void start(FeatureManager... managers) {
        program = generateProgram();
        if(runtime == null) runtime = new AutoautoRuntime(managers);

        runtime.setProgram(program);

        started = true;
    }

    @Override
    public final void loop() {
        assert started == true;
        runtime.loop();
    }

    @Override
    public final void stop() {
    }
}
