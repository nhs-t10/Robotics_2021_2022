package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.State;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.VariableReference;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;

public class FunctionDefStatement extends Statement {
    public AutoautoValue fName;

    public AutoautoFunction func;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static FunctionDefStatement J (AutoautoValue functionName, AutoautoValue[] args, State val) {
        return new FunctionDefStatement(functionName, args, val);
    }
    public static FunctionDefStatement J (String functionName, AutoautoValue[] args, State val) {
        return new FunctionDefStatement(functionName, args, val);
    }
    public FunctionDefStatement(AutoautoValue fName, AutoautoValue[] args, State body) {
        this.fName = fName;

        String[] a = new String[args.length];
        for(int i = 0; i < args.length; i++) {
            if(args[i] instanceof VariableReference) a[i] = ((VariableReference)args[i]).getName();
            else if(args[i] instanceof AutoautoPrimitive) a[i] = args[i].getString();
            else a[i] = "";
        }

        this.func = new AutoautoFunction(body, a);
    }

    public FunctionDefStatement(AutoautoValue fName, AutoautoFunction f) {
        this.fName = fName;
        this.func = f;
    }

    public FunctionDefStatement(String fName, AutoautoValue[] args, State val) {
        this(new AutoautoString(fName), args, val);
    }

    public void init() {
        this.fName.init();
        this.func.init();
    }

    @Override
    public FunctionDefStatement clone() {
        FunctionDefStatement c = new FunctionDefStatement(fName.clone(), func.clone());
        c.setLocation(location);
        return c;
    }

    public void loop() {
        fName.loop();
        this.func.loop();
        this.scope.put(this.fName.getResolvedValue().getString(), func.getResolvedValue());
    }

    public String toString() {
        return "function " + this.fName.toString() + " = " + this.func.toString();
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        this.fName.setScope(scope);
        this.func.setScope(scope);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
