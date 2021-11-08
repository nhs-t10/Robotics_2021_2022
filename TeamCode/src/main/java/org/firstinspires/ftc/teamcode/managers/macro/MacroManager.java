package org.firstinspires.ftc.teamcode.managers.macro;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.util.HashMap;

/**
 * Manages autoauto-based macros
 */
public class MacroManager extends FeatureManager {
    private final FeatureManager[] managers;
    private HashMap<String, Macro> macros;
    
    private Macro runningMacro;
    private MacroRunnerThread runner;

    public MacroManager(FeatureManager... managers) {
        FeatureManager[] managersIncludingSelf = addSelfToManagersIfRequired(managers);

        this.macros = new HashMap<>();
        this.managers = managersIncludingSelf;
    }

    private FeatureManager[] addSelfToManagersIfRequired(FeatureManager[] managers) {
        FeatureManager[] managersIncludingSelf = new FeatureManager[managers.length + 1];

        boolean managersArrayHasMacroManager = false;
        for(int i = 0; i < managers.length; i++) {
            managersIncludingSelf[i] = managers[i];
            if(managers[i] instanceof MacroManager) managersArrayHasMacroManager = true;
        }
        if(!managersArrayHasMacroManager) {
            managersIncludingSelf[managers.length] = this;
        } else {
            //fill it in to ensure no nullreferencerrors. This will be filtered later, so we don't have to worry about duplicates
            managersIncludingSelf[managers.length] = managersIncludingSelf[managers.length - 1];
        }
        return managersIncludingSelf;
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

            runner.start();
        }
    }
    public void stopMacro() {
            runningMacro = null;
            if(runner != null) runner.stopMacro();
            runner = null;
    }
    public boolean isMacroRunning() {
        return runningMacro != null;
    }
}
