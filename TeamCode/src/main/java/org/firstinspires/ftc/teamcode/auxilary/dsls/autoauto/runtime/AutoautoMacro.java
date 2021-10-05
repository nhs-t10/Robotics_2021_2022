package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.macro.Macro;

public abstract class AutoautoMacro extends Macro {
    public static AutoautoProgram program;

    private AutoautoRuntime runtime = null;
    private boolean forceStop;

    @Override
    public final void start(FeatureManager... managers) {
        if(runtime == null) {
            try {
                runtime = new AutoautoRuntime(program, managers);
            } catch (ManagerSetupException e) {
                FeatureManager.logger.log(e.toString());
            }
        } else {
            try {
                runtime.setProgram(program);
            } catch (ManagerSetupException e) {
                e.printStackTrace();
            }
        }
        
    }

    @Override
    public final void loop() {
        if(!forceStop && runtime.getCurrentStatepath().equals("end")) {
            if(runtime != null) runtime.loop();
        }
    }

    @Override
    public void stop() {
        forceStop = true;
    }
}
