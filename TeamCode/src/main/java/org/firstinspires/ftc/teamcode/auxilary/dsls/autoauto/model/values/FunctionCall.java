package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionCall extends AutoautoValue {
    public AutoautoValue[] args;
    public String name;


    public AutoautoPrimitive returnValue;

    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public FunctionCall(String n, AutoautoValue[] a) {
        this.name = n;
        this.args = a;
    }

    public static FunctionCall M(String n, AutoautoValue[] a) {
        return new FunctionCall(n, a);
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        for(int i = args.length - 1; i >= 0; i--) this.args[i].setScope(scope);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    public void init() {
        for(int i = args.length - 1; i >= 0; i--) {
            this.args[i].init();
        }
    }

    public void loop() throws AutoautoNameException {
        AutoautoValue val = scope.get(name);
        if(val == null) throw new AutoautoNameException("`" + this.name + "` is undefined" + AutoautoProgram.formatStack(location));

        if(!(val instanceof AutoautoCallableValue)) throw new AutoautoNameException("`" + name + "` is not a function" + AutoautoProgram.formatStack(location));

        AutoautoCallableValue fn = (AutoautoCallableValue)val;

        //set scope! this makes nested functions work
        ((AutoautoValue)fn).setScope(scope);

        //set the location that it's being called from. for error logging purposes
        if(val.getLocation() == null) val.setLocation(this.location);

        for(int i = this.args.length - 1; i >= 0; i--) this.args[i].loop();

        String[] argNames = fn.getArgNames();
        AutoautoPrimitive[] argsResolved = new AutoautoPrimitive[Math.max(args.length, argNames.length)];

        for(int i = argsResolved.length - 1; i >= 0; i--) {
            if(i < args.length && args[i] instanceof TitledArgument) {
                String argTitle = ((TitledArgument)args[i]).getResolvedValue().title.getString();
                int index = Arrays.asList(argNames).indexOf(argTitle);
                AutoautoPrimitive resolvedArg = ((TitledArgument)this.args[i]).getResolvedValue().value;
                if(index != -1) argsResolved[index] = resolvedArg;
            } else if(i < args.length) {
                argsResolved[i] = args[i].getResolvedValue();
            }

            if(argsResolved[i] == null) argsResolved[i] = new AutoautoUndefined();
        }

        this.returnValue = fn.call(argsResolved);
    }

    @Override
    public String getString() {
        return returnValue.getString();
    }

    @Override
    public FunctionCall clone() {
        AutoautoValue[] argsCloned = new AutoautoValue[args.length];
        for(int i = 0; i < argsCloned.length; i++) argsCloned[i] = args[i].clone();

        FunctionCall c = new FunctionCall(name, argsCloned);
        c.setLocation(location);
        return c;
    }

    @NotNull
    public AutoautoPrimitive getResolvedValue() {
        return this.returnValue;
    }

    @NotNull
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.name + "(");
        for(AutoautoValue arg : args) {
            if(arg == null) str.append("<null>");
            else str.append(arg.toString() + ", ");
        }
        str.append(")");
        return str.toString();
    }
}
