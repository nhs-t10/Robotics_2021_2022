package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUndefined;
import org.jetbrains.annotations.NotNull;

public abstract class NativeFunction extends AutoautoPrimitive implements AutoautoCallableValue {
    public String name;

    private AutoautoRuntimeVariableScope scope;
    private Location location;
    private AutoautoPrimitive executionContext = new AutoautoUndefined();

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
    }

    @NotNull
    public String getString() {
        return "<native autoauto function " + getClass().getSimpleName() + ">";
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String getJSONString() {
        return PaulMath.JSONify("[function NativeFunction]");
    }

    @Override
    public NativeFunction clone() {
        return this;
    }

    @Override
    public void setExecutionContext(AutoautoPrimitive v) {
        this.executionContext = v;
    }
}
