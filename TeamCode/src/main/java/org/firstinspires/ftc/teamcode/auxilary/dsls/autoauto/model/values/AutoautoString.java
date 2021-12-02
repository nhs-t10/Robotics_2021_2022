package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgramElement;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.jetbrains.annotations.NotNull;

public class AutoautoString extends AutoautoPrimitive {
    public String value;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static AutoautoString U (String value) {
        return new AutoautoString(value);
    }

    public AutoautoString(String value) {
        this.value = value;
    }
    public void loop() {}

    @NotNull
    @Override
    public AutoautoPrimitive getResolvedValue() {
        return this;
    }
    @NotNull
    public String toString() {
        return PaulMath.JSONify(value);
    }

    public String getString() {
        return value;
    }

    @Override
    public AutoautoString clone() {
        AutoautoString c = new AutoautoString(value);
        c.setLocation(location);
        return c;
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

    @Override
    public String getJSONString() {
        return PaulMath.JSONify(value);
    }


    public static AutoautoString fromJSON(String str) {
        String trim = str.trim();
        if(trim.startsWith("\"")) trim = trim.substring(1, trim.length() - 1);

        return new AutoautoString(PaulMath.unescapeString(trim));
    }
}
