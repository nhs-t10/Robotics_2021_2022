package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPropertyBearingObject;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.PrototypePropertyDescriptor;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.universal.UniversalPrototype;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class AutoautoPrimitive extends AutoautoValue implements AutoautoPropertyBearingObject {

    public AutoautoPrimitive() {
        setPrototype(UniversalPrototype.getMap());
    }
    @NotNull
    @Override
    public final AutoautoPrimitive getResolvedValue() {
        return this;
    }
    public abstract String getJSONString();
    @Override
    public abstract AutoautoPrimitive clone();

    private final HashMap<String, PrototypePropertyDescriptor> prototype = new HashMap<>();

    //these are primitives, so they don't need to do anything for init/loop :)
    public final void init() { }
    public final void loop() throws AutoautoNameException { }

    //prototype implementation!
    public AutoautoPrimitive getProperty(AutoautoPrimitive key) {
        //to make the method easier to read, declare 1 undefined up here
        AutoautoUndefined undefined = new AutoautoUndefined();

        String keyStr = key.getString();

        PrototypePropertyDescriptor descriptor = prototype.get(keyStr);
        if(descriptor == null) return undefined;

        //if there's a getter, invoke it with this value as an argument & return that.
        if(descriptor.getter != null) return descriptor.getter.call(this, new AutoautoPrimitive[0]);

        //if there's no getter, get the value directly.
        //return undefined instead of a null.
        AutoautoPrimitive value = descriptor.value;
        if(value == null) return undefined;

        return value;
    }
    public boolean hasProperty(AutoautoPrimitive key) {
        return prototype.containsKey(key.getString());
    }

    public void setProperty(AutoautoPrimitive key, AutoautoPrimitive value) {
        String keyStr = key.getString();
        PrototypePropertyDescriptor descriptor = prototype.get(keyStr);

        //if there's no entry in the prototype, just make a plain one
        if(descriptor == null) prototype.put(keyStr, new PrototypePropertyDescriptor(value));

        //if one exists & it has a setter, invoke it
        else if(descriptor.setter != null) descriptor.setter.call(this, new AutoautoPrimitive[] {value});

        //if the descriptor doesn't have a setter, replace it with a new descriptor. This stops weird errors with getters.
        else prototype.put(keyStr, new PrototypePropertyDescriptor(value));
    }

    public void deleteProperty(AutoautoPrimitive key) {
        //you can't delete from a prototype-- if you could, then people could delete `toString()`!
    }


    //flatten the prototype that we get. We don't want to have recursion errors from getting prototype properties!
    public void setPrototype(HashMap<String, PrototypePropertyDescriptor> p) {
        for(String k : p.keySet()) {
            prototype.put(k, p.get(k));
        }
    }
}
