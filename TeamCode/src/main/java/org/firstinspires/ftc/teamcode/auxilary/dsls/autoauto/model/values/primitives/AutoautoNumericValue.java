package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.number.NumericPrototype;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.ManagerSetupException;
import org.jetbrains.annotations.NotNull;

public class AutoautoNumericValue extends AutoautoPrimitive implements AutoautoCallableValue {
    public float value;

    private Location location;
    private AutoautoRuntimeVariableScope scope;

    public static AutoautoNumericValue C(float value) {
        return new AutoautoNumericValue(value);
    }
    public static AutoautoNumericValue C(double value) {
        return new AutoautoNumericValue(value);
    }

    public AutoautoNumericValue(float value) {
        setPrototype(NumericPrototype.getMap());
        this.value = value;
    }
    public AutoautoNumericValue(double value) {
        this((float)value);
    }

    public float getFloat() {
        return value;
    }

    public String toString() {
        return getString();
    }

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

    @NotNull
    @Override
    public String getString() {
        if((int)value == value) return (int)value + "";
        else return value + "";
    }

    @Override
    public AutoautoNumericValue clone() {
        AutoautoNumericValue c = new AutoautoNumericValue(value);
        c.setLocation(location);
        return c;
    }

    @Override
    public String getJSONString() {
        return value + "";
    }


    /*
    * Numbers are callable ONLY so both Java-esque <code>str.length()</code> and JS-esque <code>str.length</code> work. They just return themself.
    */

    @Override
    public String[] getArgNames() { return new String[0];}
    @Override
    public AutoautoPrimitive call(AutoautoPrimitive thisValue, AutoautoPrimitive[] args) { return this; }
}
