package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.jetbrains.annotations.NotNull;

public class ResolvedTitledArg extends AutoautoPrimitive {
    public AutoautoPrimitive title;
    public AutoautoPrimitive value;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public ResolvedTitledArg(AutoautoPrimitive title, AutoautoPrimitive value) {
        this.title = title;
        this.value = value;
    }

    @Override
    public String getJSONString() {
        return "{\"title\":" + title.getJSONString() + ",\"value:\"" + value.getJSONString() + "}";
    }

    @NotNull
    @Override
    public String getString() {
        return value.getString();
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        title.setScope(scope);
        value.setScope(scope);
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
    public ResolvedTitledArg clone() {
        return new ResolvedTitledArg(title.clone(), value.clone());
    }

    public String toString() {
        return title + " = " + value;
    }
}
