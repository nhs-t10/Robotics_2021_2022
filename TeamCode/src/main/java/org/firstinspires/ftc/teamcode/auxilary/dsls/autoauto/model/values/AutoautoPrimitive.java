package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class AutoautoPrimitive extends AutoautoValue implements AutoautoPropertyBearingObject {
    @NotNull
    @Override
    public AutoautoPrimitive getResolvedValue() {
        return this;
    }
    public abstract String getJSONString();
    @Override
    public abstract AutoautoPrimitive clone();

    private final HashMap<String, AutoautoPrimitive> elems = new HashMap<>();

    //these are primitives, so they don't need to do anything for init/loop :)
    public void init() { }
    public void loop() throws AutoautoNameException { }

    //prototype implementation
    public AutoautoPrimitive getProperty(AutoautoPrimitive key) {
        String keyStr = key.getString();
        if(!elems.containsKey(keyStr)) return new AutoautoUndefined();

        return elems.get(keyStr);
    }
    public boolean hasProperty(AutoautoPrimitive key) {
        return elems.containsKey(key.getString());
    }

    public void setProperty(AutoautoPrimitive key, AutoautoPrimitive value) {
        elems.put(key.getString(), value);
    }

    public void deleteProperty(AutoautoPrimitive key) {
        elems.remove(key.getString());
    }
}
