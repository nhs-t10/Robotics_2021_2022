package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTailedValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.VariableReference;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;

public class LetStatement extends Statement {
    public AutoautoValue variable;

    public AutoautoValue value;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static LetStatement D (VariableReference var, AutoautoValue val) {
        return new LetStatement(var, val);
    }
    public static LetStatement D (String var, AutoautoValue val) {
        return new LetStatement(var, val);
    }
    public static LetStatement D (AutoautoTailedValue var, AutoautoValue val) {
        return new LetStatement(var, val);
    }

    public LetStatement(VariableReference var, AutoautoValue val) {
        this(var.getName(), val);
    }

    public LetStatement(String var, AutoautoValue val) {
        this.variable = new AutoautoString(var);
        this.value = val;
    }

    public LetStatement(AutoautoTailedValue var, AutoautoValue val) {
        this.variable = var;
        this.value = val;
    }

    public void init() {
        this.value.init();
    }

    @Override
    public LetStatement clone() {
        LetStatement c;
        if(variable instanceof AutoautoTailedValue) {
            c = new LetStatement((AutoautoTailedValue) variable.clone(), value.clone());
        } else {
            c = new LetStatement(variable.getString(), value.clone());
        }
        c.setLocation(location);
        return c;
    }

    public void loop() {
        this.value.loop();

        //if it's a tailed value, drill down so we can set the variable as a property instead of in the current scope.
        if(variable instanceof AutoautoTailedValue) {
            AutoautoValue settingContext = ((AutoautoTailedValue) variable).head;
            AutoautoValue property = ((AutoautoTailedValue) variable).tail;

            settingContext.loop();
            property.loop();
            settingContext.getResolvedValue().setProperty(property.getResolvedValue(), value.getResolvedValue());
        } else {
            this.scope.put(this.variable.getResolvedValue().getString(), value.getResolvedValue());
        }
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
        this.variable.setScope(scope);
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
