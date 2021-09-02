package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import androidx.annotation.Nullable;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.DefunFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.ExistsFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.LengthFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.LogFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.ReturnFunction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

public class AutoautoRuntimeVariableScope {
    public HashMap<String, StoredAutoautoValue> variables;

    @Nullable
    public AutoautoRuntimeVariableScope parentScope;

    @Nullable
    private AutoautoRuntimeVariableScope rootScope;

    public AutoautoRuntimeVariableScope() {
        this.variables = new HashMap<String, StoredAutoautoValue>();
    }
    public AutoautoRuntimeVariableScope(@NotNull AutoautoRuntimeVariableScope parentScope) {
        this.parentScope = parentScope;
        this.rootScope = parentScope.getRoot();
        this.variables = new HashMap<String, StoredAutoautoValue>();
    }

    public Set<String> getKeys() {
        return variables.keySet();
    }

    public void put(String s, AutoautoPrimitive v) {
        StoredAutoautoValue value = getStored(s);

        if(value == null) {
            this.variables.put(s, new StoredAutoautoValue(v));
        } else {
            if(value.systemManaged) {
                throw new IllegalArgumentException("Variable " + s + " is a system variable");
            } else if(value.readOnly) {
                throw new IllegalArgumentException("Variable " + s + " is a read-only variable");
            };
            //if it's in this scope...
            if(this.variables.containsKey(s)) {
                //... set it
                value.value = v;
                this.variables.put(s, value);
            } // if not...
            else {
                //... propagate up. This gets inefficient with a large number of nested scopes; should be fine for now, but should be fixed
                // TODO: optimize nested scopes
                parentScope.put(s, v);
            }
        }
    }

    public AutoautoPrimitive get(String s) {
        if(this.variables.containsKey(s)) return this.variables.get(s).value;
        else if(parentScope != null) return parentScope.get(s);
        else return null;
    }

    private StoredAutoautoValue getStored(String s) {
        if(this.variables.containsKey(s)) return this.variables.get(s);
        else if(parentScope != null) return parentScope.getStored(s);
        else return null;
    }

    public void systemRemove(String s) {
        if(this.variables.containsKey(s)) this.variables.remove(s);
        else if(parentScope != null) parentScope.systemRemove(s);
    }

    public void systemSet(String s, AutoautoPrimitive v) {
        StoredAutoautoValue value = getStored(s);

        if(value == null) {
            value = new StoredAutoautoValue(v);
            value.systemManaged = true;
            this.variables.put(s, value);
        } else {
            if(!value.systemManaged) {
                throw new IllegalArgumentException("Variable " + s + " is not a system variable");
            }
            if(value.readOnly) {
                throw new IllegalArgumentException("Variable " + s + " is a read-only variable");
            }
            //if it's in this scope...
            if(this.variables.containsKey(s)) {
                //... set it
                value.value = v;
                this.variables.put(s, value);
            } // if not
            else {
                //... propagate up. This gets inefficient with a large number of nested scopes; should be fine for now, but should be fixed
                // TODO: optimize nested scopes
                parentScope.systemSet(s, v);
            }
        }
    }

    /**
     * Init "syntactical sugar" variables, such as `pi`, that make it easier for coders to do their job
     */
    public void initSugarVariables() {
        this.systemSet("pi", new AutoautoNumericValue((float) Math.PI));
        this.systemSet("e", new AutoautoNumericValue((float) Math.E));

        this.systemSet("True", new AutoautoBooleanValue(true));
        this.systemSet("TRUE", new AutoautoBooleanValue(true));
        this.systemSet("False", new AutoautoBooleanValue(false));
        this.systemSet("FALSE", new AutoautoBooleanValue(false));

        this.systemSet("undefined", new AutoautoUndefined());
        this.systemSet("null", new AutoautoUndefined());
        this.systemSet("delete", new AutoautoUndefined());
    }

    public void initBuiltinFunctions(AutoautoRuntime runtime) {
        this.systemSet("log", new LogFunction());
        this.systemSet("print", new LogFunction());

        DefunFunction defun = new DefunFunction(runtime);
        defun.setScope(runtime.globalScope);
        this.systemSet("defun", defun);

        this.systemSet("return", new ReturnFunction(runtime));
        this.systemSet("length", new LengthFunction());
        this.systemSet("exists", new ExistsFunction());
    }

    public boolean isTopLevel() {
        return this.parentScope == null;
    }

    public AutoautoRuntimeVariableScope getRoot() {
        if(rootScope == null) return this;
        else return rootScope;
    }

    public void remove(String s) {
        if(this.variables.containsKey(s)) this.variables.remove(s);
        else if(parentScope != null) parentScope.remove(s);
    }
}
