package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.jetbrains.annotations.NotNull;

public abstract class NativeFunction extends AutoautoPrimitive implements AutoautoCallableValue {
    public String name;

    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public AutoautoPrimitive getResolvedValue() {
        return null;
    }

    @Override
    public void loop() {}
    @Override
    public void init() {}

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
    }

    public String getString() {
        return null;
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
}
