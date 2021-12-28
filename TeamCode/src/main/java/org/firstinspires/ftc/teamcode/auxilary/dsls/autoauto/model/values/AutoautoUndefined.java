package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;
import org.jetbrains.annotations.NotNull;

public class AutoautoUndefined extends AutoautoPrimitive {
    public static final int NONEXISTENT_VARIABLE = 1;

    private Location location;

    public int source;
    public AutoautoUndefined(int source) {
        this.source = source;
    }

    public AutoautoUndefined() { this(0); }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return null;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {

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
        return "undefined";
    }

    @NotNull
    @Override
    public AutoautoPrimitive getResolvedValue() {
        return this;
    }

    @Override
    public String getJSONString() {
        return "null";
    }

    @Override
    public AutoautoUndefined clone() {
        AutoautoUndefined c = new AutoautoUndefined();
        c.setLocation(location);
        return c;
    }
    public String toString() {
        return "undefined";
    }

    //make sure people don't try to set properties on undefined
    public AutoautoPrimitive getProperty(AutoautoPrimitive key) {
        return this;
    }
    public boolean hasProperty(AutoautoPrimitive key) {
        return false;
    }

    public void setProperty(AutoautoPrimitive key, AutoautoPrimitive value) {}

    public void deleteProperty(AutoautoPrimitive key) {}
}
