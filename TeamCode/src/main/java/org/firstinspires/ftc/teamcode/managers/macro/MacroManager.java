package org.firstinspires.ftc.teamcode.managers.macro;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Manages autoauto-based macros
 */
public class MacroManager extends FeatureManager {
    private final FeatureManager[] managers;
    private final HashMap<String, Macro> macros;
    
    @Nullable
    private Macro runningMacro;
    @Nullable
    private MacroRunnerThread runner;

    /**
     * Construct a MacroManager. Every other manager that the macro runtimes need should be included.
     * Usually, this means that the {@code MacroManager} should be declared last, since it needs everything else to be initialized before it.
     * @param managers Managers for macro runtimes to use.
     */
    public MacroManager(FeatureManager... managers) {
        FeatureManager[] managersIncludingSelf = addSelfToManagersIfRequired(managers);

        this.macros = new HashMap<>();
        this.managers = managersIncludingSelf;
    }

    /**
     * If there isn't a MacroManager in the given array, make the MacroManager add itself.
     *
     * @param managers an array of managers. Maybe it has a MacroManager, maybe not.
     * @return an array of the same managers, but now with a MacroManager in it.
     */
    private FeatureManager[] addSelfToManagersIfRequired(FeatureManager[] managers) {
        FeatureManager[] managersIncludingSelf = new FeatureManager[managers.length + 1];

        boolean managersArrayHasMacroManager = false;
        for(int i = 0; i < managers.length; i++) {
            managersIncludingSelf[i] = managers[i];
            if(managers[i] instanceof MacroManager) managersArrayHasMacroManager = true;
        }
        if(managersArrayHasMacroManager) {
            return managers;
        } else {
            managersIncludingSelf[managers.length] = this;
            return managersIncludingSelf;
        }
    }

    /**
     * Register a Macro with a given name. Usually, the macro is an auto-generated {@link org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoMacro}.
     * <h2>Usage Example</h2>
     * <pre><code>
     *     macroManager.registerMacro("moveLeft", new AutoautoFileName__macro_autoauto());
     * </code></pre>
     * @param name a name, to be referred to later. Must be unique.
     * @param macro the macro to associate with the name.
     */
    public void registerMacro(String name, Macro macro) {
        if(!this.macros.containsKey(name)) {
            this.macros.put(name, macro);
        } else {
            throw new IllegalArgumentException(name + " already exists!");
        }
    }

    /**
     * <p>Make a macro start running. If there is already a macro running in this manager, this method is ignored.</p>
     * <p>Note that this is an <u>asynchronous</u> method, meaning that the method will complete before the action does.
     * Something like {@code runMacro("test"); toggleClawOpen()} will open the claw <u>right away</u>. It does <u>not</u> wait for the macro to complete.
     * </p>
     *
     * @see #isMacroRunning()
     * @see #stopMacro()
     * @param name the unique name of the macro, originally specified by the call to {@link #registerMacro(String, Macro)}
     */
    public void runMacro(String name) {
        if(runningMacro != null) {
            Macro macro = macros.get(name);
            if (macro != null) {
                runningMacro = macro;
                macro.start(managers);

                runner = new MacroRunnerThread(macro);
                runner.start();
            }
        }
    }
    /**
     * <p>If a macro is running in this manager, stop it. If there is no macro running, nothing happens.</p>
     */
    public void stopMacro() {
        synchronized (macros) {
            runningMacro = null;
        }
        if(runner != null) runner.stopMacro();
        runner = null;
    }

    /**
     * Check whether a macro is running in this manager.
     *
     * @return {@code true} if a macro is running; {@code false} otherwise.
     */
    public boolean isMacroRunning() {
        synchronized (macros) {
            return runningMacro != null;
        }
    }
}
