package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Statepath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntime;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

public class DefunFunction extends NativeFunction {
    public String name = "defun";
    public int argCount = 1;

    private AutoautoRuntime runtime;

    public DefunFunction(AutoautoRuntime runtime) {
        this.runtime = runtime;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) {
        if(args.length == 0) throw new AutoautoArgumentException("0 arguments provided to defun(); 1 argument required" + AutoautoProgram.formatStack(getLocation()));

        String pathName = args[0].getString();
        Statepath statepath = runtime.program.paths.get(pathName);
        if(statepath == null) throw new AutoautoArgumentException("Unknown statepath `" + pathName + "` provided to defun()" + AutoautoProgram.formatStack(getLocation()));

        AutoautoFunction func = new AutoautoFunction(statepath.states[0]);
        func.setLocation(getLocation());
        func.setScope(getScope());

        return func;
    }
}