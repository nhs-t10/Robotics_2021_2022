package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.jetbrains.annotations.NotNull;

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

        //array values are recovered using a function-like syntax
        if(val instanceof AutoautoTable) {
            AutoautoTable arrayValue = (AutoautoTable)val;
            for(int i = this.args.length - 1; i >= 0; i--) this.args[i].loop();

            if(args.length != 1 && args.length != 2) throw new AutoautoArgumentException("Array accessors must have 1 or 2 arguments"+ AutoautoProgram.formatStack(location));

            args[0].loop();
            AutoautoPrimitive key = args[0].getResolvedValue();

            if(args.length == 1) {
                this.returnValue = arrayValue.get(key);
            } else if(args.length == 2) {
                if(args[1].toString().equals("var<delete>")) {
                    this.returnValue = arrayValue.get(key);
                    arrayValue.delete(key);
                } else {
                    args[1].loop();
                    arrayValue.set(key, args[1].getResolvedValue());
                    this.returnValue = args[1].getResolvedValue();
                }
            }

            return;
        }

        if(val instanceof AutoautoString) {
            this.returnValue = new AutoautoString("");
            if(args.length == 1 || args.length == 2) {
                args[0].loop();
                AutoautoPrimitive index = args[0].getResolvedValue();
                if(!(index instanceof AutoautoNumericValue)) throw new AutoautoArgumentException("String accessors must be numeric; attempt with " + index.getString() + AutoautoProgram.formatStack(location));
                int indexNum = (int)((AutoautoNumericValue) index).getFloat();
                String str = val.getString();
                if(indexNum < 0 || indexNum >= str.length()) throw new AutoautoArgumentException("Illegal index " + indexNum + " for a string of length " + str.length() + AutoautoProgram.formatStack(location));
                this.returnValue = new AutoautoString(str.charAt(indexNum) + "");
                if(args.length == 2) {
                    args[1].loop();
                    AutoautoPrimitive index2 = args[1].getResolvedValue();
                    if(!(index2 instanceof AutoautoNumericValue)) throw new AutoautoArgumentException("String accessors must be numeric; attempt with " + index2.getString() + AutoautoProgram.formatStack(location));
                    int index2Num = (int)((AutoautoNumericValue) index2).getFloat();
                    if(index2Num < 0 || index2Num >= str.length()) throw new AutoautoArgumentException("Illegal index " + index2Num + " for a string of length " + str.length() + AutoautoProgram.formatStack(location));
                    this.returnValue = new AutoautoString(str.substring(indexNum, index2Num) + "");
                }
            }
            return;
        }

        if(!(val instanceof AutoautoCallableValue)) throw new AutoautoNameException("`" + name + "` is not a function" + AutoautoProgram.formatStack(location));

        AutoautoCallableValue fn = (AutoautoCallableValue)val;

        //set scope! this makes nested functions work
        ((AutoautoValue)fn).setScope(scope);

        //set the location that it's being called from. for error logging purposes
        if(val.getLocation() == null) val.setLocation(this.location);

        for(int i = this.args.length - 1; i >= 0; i--) this.args[i].loop();

        AutoautoPrimitive[] argsResolved = new AutoautoPrimitive[args.length];
        for(int i = this.args.length - 1; i >= 0; i--) argsResolved[i] = this.args[i].getResolvedValue();

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
