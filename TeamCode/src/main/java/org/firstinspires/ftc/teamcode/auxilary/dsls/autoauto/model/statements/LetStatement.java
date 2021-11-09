package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.VariableReference;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;

public class LetStatement extends Statement {
    public String variable;

    public AutoautoValue value;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static LetStatement D (VariableReference var, AutoautoValue val) {
        return new LetStatement(var, val);
    }
    public static LetStatement D (String var, AutoautoValue val) {
        return new LetStatement(var, val);
    }

    public LetStatement(VariableReference var, AutoautoValue val) {
        this.variable = var.getName();
        this.value = val;
    }

    public LetStatement(String var, AutoautoValue val) {
        this.variable = var;
        this.value = val;
    }

    public void init() {
        this.value.init();
    }

    @Override
    public LetStatement clone() {
        LetStatement c = new LetStatement(variable, value.clone());
        c.setLocation(location);
        return c;
    }

    public void loop() {
        this.value.loop();
        //special keyword delete
        if(this.value instanceof VariableReference && ((VariableReference)this.value).getName().equals("delete")) {
            this.scope.remove(this.variable);
        }
        this.scope.put(this.variable, value.getResolvedValue());
    }

    public String toString() {
        return "let " + this.variable + " = " + this.value.toString();
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        this.value.setScope(scope);
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
