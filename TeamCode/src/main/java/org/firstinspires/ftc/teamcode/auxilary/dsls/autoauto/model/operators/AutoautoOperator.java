package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.operators;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgramElement;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;
import org.jetbrains.annotations.NotNull;

public abstract class AutoautoOperator implements AutoautoProgramElement {
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static AutoautoOperator get(String name) {
        switch(name) {
            case "+": return new PlusOperator();
            case "-": return new MinusOperator();
            case "*": return new TimesOperator();
            case "%": return new ModuloOperator();
            case "/": return new DivisionOperator();
            case "^":
            case "**": return new ExponentiationOperator();
            case "<": return new LessThanOperator();
            case "<=": return new LequalsOperator();
            case "==": return new EqualsOperator();
            case "!=": return new NotEqualsOperator();
            case ">=": return new GrequalsOperator();
            case ">": return new GreaterThanOperator();
        }
        throw new IllegalArgumentException("Bad operator " + name);
    }

    @NotNull
    public final AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoPrimitive right, boolean doCasting) {
        assert doCasting;

        AutoautoPrimitive r = null;

        if(r == null && left instanceof AutoautoBooleanValue && right instanceof AutoautoBooleanValue) r = eval((AutoautoBooleanValue)left, (AutoautoBooleanValue)right);
        if(r == null && left instanceof AutoautoFunction && right instanceof AutoautoBooleanValue) r = eval((AutoautoFunction)left, (AutoautoBooleanValue)right);
        if(r == null && left instanceof AutoautoNumericValue && right instanceof AutoautoBooleanValue) r = eval((AutoautoNumericValue)left, (AutoautoBooleanValue)right);
        if(r == null && left instanceof AutoautoString && right instanceof AutoautoBooleanValue) r = eval((AutoautoString)left, (AutoautoBooleanValue)right);
        if(r == null && left instanceof AutoautoTable && right instanceof AutoautoBooleanValue) r = eval((AutoautoTable)left, (AutoautoBooleanValue)right);
        if(r == null && left instanceof AutoautoUndefined && right instanceof AutoautoBooleanValue) r = eval((AutoautoUndefined)left, (AutoautoBooleanValue)right);

        if(r == null && left instanceof AutoautoBooleanValue && right instanceof AutoautoFunction) r = eval((AutoautoBooleanValue)left, (AutoautoFunction)right);
        if(r == null && left instanceof AutoautoFunction && right instanceof AutoautoFunction) r = eval((AutoautoFunction)left, (AutoautoFunction)right);
        if(r == null && left instanceof AutoautoNumericValue && right instanceof AutoautoFunction) r = eval((AutoautoNumericValue)left, (AutoautoFunction)right);
        if(r == null && left instanceof AutoautoString && right instanceof AutoautoFunction) r = eval((AutoautoString)left, (AutoautoFunction)right);
        if(r == null && left instanceof AutoautoTable && right instanceof AutoautoFunction) r = eval((AutoautoTable)left, (AutoautoFunction)right);
        if(r == null && left instanceof AutoautoUndefined && right instanceof AutoautoFunction) r = eval((AutoautoUndefined)left, (AutoautoFunction)right);

        if(r == null && left instanceof AutoautoBooleanValue && right instanceof AutoautoNumericValue) r = eval((AutoautoBooleanValue)left, (AutoautoNumericValue)right);
        if(r == null && left instanceof AutoautoFunction && right instanceof AutoautoNumericValue) r = eval((AutoautoFunction)left, (AutoautoNumericValue)right);
        if(r == null && left instanceof AutoautoNumericValue && right instanceof AutoautoNumericValue) r = eval((AutoautoNumericValue)left, (AutoautoNumericValue)right);
        if(r == null && left instanceof AutoautoString && right instanceof AutoautoNumericValue) r = eval((AutoautoString)left, (AutoautoNumericValue)right);
        if(r == null && left instanceof AutoautoTable && right instanceof AutoautoNumericValue) r = eval((AutoautoTable)left, (AutoautoNumericValue)right);
        if(r == null && left instanceof AutoautoUndefined && right instanceof AutoautoNumericValue) r = eval((AutoautoUndefined)left, (AutoautoNumericValue)right);
        if(r == null && left instanceof AutoautoBooleanValue && right instanceof AutoautoString) r = eval((AutoautoBooleanValue)left, (AutoautoString)right);

        if(r == null && left instanceof AutoautoFunction && right instanceof AutoautoString) r = eval((AutoautoFunction)left, (AutoautoString)right);
        if(r == null && left instanceof AutoautoNumericValue && right instanceof AutoautoString) r = eval((AutoautoNumericValue)left, (AutoautoString)right);
        if(r == null && left instanceof AutoautoString && right instanceof AutoautoString) r = eval((AutoautoString)left, (AutoautoString)right);
        if(r == null && left instanceof AutoautoTable && right instanceof AutoautoString) r = eval((AutoautoTable)left, (AutoautoString)right);
        if(r == null && left instanceof AutoautoUndefined && right instanceof AutoautoString) r = eval((AutoautoUndefined)left, (AutoautoString)right);

        if(r == null && left instanceof AutoautoBooleanValue && right instanceof AutoautoTable) r = eval((AutoautoBooleanValue)left, (AutoautoTable)right);
        if(r == null && left instanceof AutoautoFunction && right instanceof AutoautoTable) r = eval((AutoautoFunction)left, (AutoautoTable)right);
        if(r == null && left instanceof AutoautoNumericValue && right instanceof AutoautoTable) r = eval((AutoautoNumericValue)left, (AutoautoTable)right);
        if(r == null && left instanceof AutoautoString && right instanceof AutoautoTable) r = eval((AutoautoString)left, (AutoautoTable)right);
        if(r == null && left instanceof AutoautoTable && right instanceof AutoautoTable) r = eval((AutoautoTable)left, (AutoautoTable)right);
        if(r == null && left instanceof AutoautoUndefined && right instanceof AutoautoTable) r = eval((AutoautoUndefined)left, (AutoautoTable)right);

        if(r == null && left instanceof AutoautoBooleanValue && right instanceof AutoautoUndefined) r = eval((AutoautoBooleanValue)left, (AutoautoUndefined)right);
        if(r == null && left instanceof AutoautoFunction && right instanceof AutoautoUndefined) r = eval((AutoautoFunction)left, (AutoautoUndefined)right);
        if(r == null && left instanceof AutoautoNumericValue && right instanceof AutoautoUndefined) r = eval((AutoautoNumericValue)left, (AutoautoUndefined)right);
        if(r == null && left instanceof AutoautoString && right instanceof AutoautoUndefined) r = eval((AutoautoString)left, (AutoautoUndefined)right);
        if(r == null && left instanceof AutoautoTable && right instanceof AutoautoUndefined) r = eval((AutoautoTable)left, (AutoautoUndefined)right);
        if(r == null && left instanceof AutoautoUndefined && right instanceof AutoautoUndefined) r = eval((AutoautoUndefined)left, (AutoautoUndefined)right);

        if(r == null && right instanceof AutoautoBooleanValue) r = eval(left, (AutoautoBooleanValue)right);
        if(r == null && right instanceof AutoautoFunction) r = eval(left, (AutoautoFunction)right);
        if(r == null && right instanceof AutoautoTable) r = eval(left, (AutoautoTable)right);
        if(r == null && right instanceof AutoautoString) r = eval(left, (AutoautoString)right);
        if(r == null && right instanceof AutoautoNumericValue) r = eval(left, (AutoautoNumericValue)right);
        if(r == null && right instanceof AutoautoUndefined) r = eval(left, (AutoautoUndefined)right);
        if(r == null && left instanceof AutoautoBooleanValue) r = eval((AutoautoBooleanValue)left, right);
        if(r == null && left instanceof AutoautoFunction) r = eval((AutoautoFunction)left, right);
        if(r == null && left instanceof AutoautoNumericValue) r = eval((AutoautoNumericValue)left, right);
        if(r == null && left instanceof AutoautoString) r = eval((AutoautoString)left, right);
        if(r == null && left instanceof AutoautoTable) r = eval((AutoautoTable)left, right);
        if(r == null && left instanceof AutoautoUndefined) r = eval((AutoautoUndefined)left, right);
        if(r == null) r = eval(left, right);

        if(r == null) throw new AutoautoArgumentException("Bad values given to `" + getOperatorStr() + "` operator" + formatStack());
        else return r;
    }

    //auto-generated overrides. Re-make these if you add a new primitive-- make sure there's an override for each pair of 2 classes.
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoBooleanValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoFunction left, AutoautoBooleanValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoBooleanValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoString left, AutoautoBooleanValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoBooleanValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoBooleanValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoBooleanValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoFunction right) { return null; }
    public AutoautoPrimitive eval(AutoautoFunction left, AutoautoFunction right) { return null; }
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoFunction right) { return null; }
    public AutoautoPrimitive eval(AutoautoString left, AutoautoFunction right) { return null; }
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoFunction right) { return null; }
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoFunction right) { return null; }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoFunction right) { return null; }
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoNumericValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoFunction left, AutoautoNumericValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoNumericValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoString left, AutoautoNumericValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoNumericValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoNumericValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoNumericValue right) { return null; }
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoString right) { return null; }
    public AutoautoPrimitive eval(AutoautoFunction left, AutoautoString right) { return null; }
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoString right) { return null; }
    public AutoautoPrimitive eval(AutoautoString left, AutoautoString right) { return null; }
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoString right) { return null; }
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoString right) { return null; }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoString right) { return null; }
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoTable right) { return null; }
    public AutoautoPrimitive eval(AutoautoFunction left, AutoautoTable right) { return null; }
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoTable right) { return null; }
    public AutoautoPrimitive eval(AutoautoString left, AutoautoTable right) { return null; }
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoTable right) { return null; }
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoTable right) { return null; }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoTable right) { return null; }
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoUndefined right) { return null; }
    public AutoautoPrimitive eval(AutoautoFunction left, AutoautoUndefined right) { return null; }
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoUndefined right) { return null; }
    public AutoautoPrimitive eval(AutoautoString left, AutoautoUndefined right) { return null; }
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoUndefined right) { return null; }
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoUndefined right) { return null; }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoUndefined right) { return null; }
    public AutoautoPrimitive eval(AutoautoBooleanValue left, AutoautoPrimitive right) { return null; }
    public AutoautoPrimitive eval(AutoautoFunction left, AutoautoPrimitive right) { return null; }
    public AutoautoPrimitive eval(AutoautoNumericValue left, AutoautoPrimitive right) { return null; }
    public AutoautoPrimitive eval(AutoautoString left, AutoautoPrimitive right) { return null; }
    public AutoautoPrimitive eval(AutoautoTable left, AutoautoPrimitive right) { return null; }
    public AutoautoPrimitive eval(AutoautoUndefined left, AutoautoPrimitive right) { return null; }
    public AutoautoPrimitive eval(AutoautoPrimitive left, AutoautoPrimitive right) { return null; }





    public abstract String getOperatorStr();
    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    public String toString() {
        return getOperatorStr();
    }

    @Override
    public AutoautoProgramElement clone() {
        return AutoautoOperator.get(this.getOperatorStr());
    }
}
