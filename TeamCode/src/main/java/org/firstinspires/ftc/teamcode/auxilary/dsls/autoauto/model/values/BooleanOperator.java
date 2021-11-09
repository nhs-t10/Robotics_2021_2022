package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.jetbrains.annotations.NotNull;

enum Type {LESS_THAN, LESS_EQUAL_THAN, EQUAL, GREATER_THAN, GREATER_EQUAL_THAN, NOT_EQUAL, FUNC_CALL, HUH}

public class BooleanOperator extends AutoautoValue {

    private final String operator;
    Type type;

    AutoautoValue a;
    AutoautoValue b;

    AutoautoBooleanValue resolvedValue;

    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static BooleanOperator T (AutoautoValue a, AutoautoValue b, String operator) {
        return new BooleanOperator(a, b, operator);
    }

    public BooleanOperator(AutoautoValue a, AutoautoValue b, String operator) {
        this.a = a;
        this.b = b;
        this.operator = operator;
        switch (operator) {
            case "<": this.type = Type.LESS_THAN; break;
            case "<=": this.type = Type.LESS_EQUAL_THAN; break;
            case ">": this.type = Type.GREATER_THAN; break;
            case ">=": this.type = Type.GREATER_EQUAL_THAN; break;
            case "==": this.type = Type.EQUAL; break;
            case "!=": this.type = Type.NOT_EQUAL; break;
        }
    }
    @NotNull
    @Override
    public AutoautoPrimitive getResolvedValue() {
        return resolvedValue;
    }

    public boolean getBoolean() {
        return resolvedValue.getBoolean();
    }

    public void init() {
        a.init();
        b.init();
    }

    public void loop() {
        a.loop();
        b.loop();

        AutoautoValue aRes = a.getResolvedValue();
        AutoautoValue bRes = b.getResolvedValue();

        //cast undefined to false
        if(aRes instanceof  AutoautoUndefined) {
            aRes = new AutoautoBooleanValue(false);
        }
        if(bRes instanceof AutoautoUndefined) {
            bRes = new AutoautoBooleanValue(false);
        }

        //string comparison
        if(aRes instanceof AutoautoString || bRes instanceof AutoautoString) {
            String a = null;
            if (aRes instanceof AutoautoString) a = ((AutoautoString) aRes).getString();
            else if (aRes instanceof AutoautoNumericValue) a = ((AutoautoNumericValue) aRes).getFloat() + "";
            else if (aRes instanceof AutoautoBooleanValue) a = ((AutoautoBooleanValue) aRes).getBoolean() + "";

            String b = null;
            if (bRes instanceof AutoautoString) b = ((AutoautoString) bRes).getString();
            else if (bRes instanceof AutoautoNumericValue) b = ((AutoautoNumericValue) bRes).getFloat() + "";
            else if (bRes instanceof AutoautoBooleanValue) b = ((AutoautoBooleanValue) bRes).getBoolean() + "";

            boolean equal = a.equals(b);

            if (type.equals(Type.EQUAL)) resolvedValue = new AutoautoBooleanValue(equal);
            else if (type.equals(Type.NOT_EQUAL)) resolvedValue = new AutoautoBooleanValue(!equal);
            else if (type.equals(Type.GREATER_EQUAL_THAN)) resolvedValue = new AutoautoBooleanValue(a.compareTo(b) >= 0);
            else if (type.equals(Type.GREATER_THAN)) resolvedValue = new AutoautoBooleanValue(a.compareTo(b) > 0);
            else if (type.equals(Type.LESS_EQUAL_THAN)) resolvedValue = new AutoautoBooleanValue(a.compareTo(b) <= 0);
            else if (type.equals(Type.LESS_THAN)) resolvedValue = new AutoautoBooleanValue(a.compareTo(b) < 0);
        } else if(aRes instanceof AutoautoNumericValue || bRes instanceof AutoautoNumericValue) {
            //if it's not a string, numeric...
            float a = 0;
            if(aRes instanceof AutoautoNumericValue) a = ((AutoautoNumericValue) aRes).getFloat();
            else if (aRes instanceof AutoautoBooleanValue) a = ((AutoautoBooleanValue) aRes).getBoolean() ? 1f : 0f;

            float b = 0;
            if(bRes instanceof AutoautoNumericValue) b = ((AutoautoNumericValue) bRes).getFloat();
            else if (bRes instanceof AutoautoBooleanValue) b = ((AutoautoBooleanValue) bRes).getBoolean() ? 1f : 0f;

            if (type.equals(Type.EQUAL)) resolvedValue = new AutoautoBooleanValue(a == b);
            else if (type.equals(Type.NOT_EQUAL)) resolvedValue = new AutoautoBooleanValue(a != b);
            else if (type.equals(Type.GREATER_EQUAL_THAN)) resolvedValue = new AutoautoBooleanValue(a >= b);
            else if (type.equals(Type.GREATER_THAN)) resolvedValue = new AutoautoBooleanValue(a > b);
            else if (type.equals(Type.LESS_EQUAL_THAN)) resolvedValue = new AutoautoBooleanValue(a <= b);
            else if (type.equals(Type.LESS_THAN)) resolvedValue = new AutoautoBooleanValue(a < b);
        } else if(aRes instanceof AutoautoBooleanValue || bRes instanceof AutoautoBooleanValue) {
            //booleans are the lowest in the implicit casting cascade
            int a = ((AutoautoBooleanValue) aRes).getBoolean() ? 1 : 0;

            int b = ((AutoautoBooleanValue) bRes).getBoolean() ? 1 : 0;

            if (type.equals(Type.EQUAL)) resolvedValue = new AutoautoBooleanValue(a == b);
            else if (type.equals(Type.NOT_EQUAL)) resolvedValue = new AutoautoBooleanValue(a != b);
            else if (type.equals(Type.GREATER_EQUAL_THAN)) resolvedValue = new AutoautoBooleanValue(a >= b);
            else if (type.equals(Type.GREATER_THAN)) resolvedValue = new AutoautoBooleanValue(a > b);
            else if (type.equals(Type.LESS_EQUAL_THAN)) resolvedValue = new AutoautoBooleanValue(a <= b);
            else if (type.equals(Type.LESS_THAN)) resolvedValue = new AutoautoBooleanValue(a < b);
        }
    }

    @Override
    public String getString() {
        return resolvedValue.getString();
    }

    @Override
    public BooleanOperator clone() {
        BooleanOperator c = new BooleanOperator(a.clone(), b.clone(), operator);
        c.setLocation(location);
        return c;
    }

    public String toString() {
        return this.a.toString() + " " + this.type.name() + " " + this.b.toString();
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return this.scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        a.setScope(scope);
        b.setScope(scope);
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
