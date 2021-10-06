package org.firstinspires.ftc.teamcode.managers.macro;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntime;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.ManagerSetupException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

import java.util.HashMap;

import javax.crypto.Mac;

/**
 * Manages autoauto-based macros
 */
public class MacroManager extends FeatureManager {
    private final FeatureManager[] managers;
    private HashMap<String, Macro> macros;
    
    private Macro runningMacro;
    private MacroRunnerThread runner;

    public MacroManager(FeatureManager... managers) {
        this.macros = new HashMap<>();
        this.managers = managers;
    }

    public void registerMacro(String name, Macro macro) {
        if(!this.macros.containsKey(name)) {
            this.macros.put(name, macro);
        } else {
            throw new IllegalArgumentException(name + " already exists!");
        }
    }
    public void runMacro(String name) {
        Macro macro = macros.get(name);
        if(macro != null) {
            runningMacro = macro;
            macro.start(managers);
            runner = new MacroRunnerThread(macro);

            (new Thread(runner)).start();
        }
    }
    public void stopMacro() {
        if(isMacroRunning()) {
            runningMacro = null;
            runner.stop();
            runner = null;
        }
    }
    public boolean isMacroRunning() {
        return runningMacro != null;
    }
}
