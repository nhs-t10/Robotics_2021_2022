package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.universal;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.PrototypePropertyDescriptor;

import java.util.HashMap;

public class UniversalPrototype {
    private static NoopFunction Noop = new NoopFunction();
    private static ToStringFunctionGetter toStringFunction = new ToStringFunctionGetter();

    public static HashMap<String, PrototypePropertyDescriptor> getMap() {
        HashMap<String, PrototypePropertyDescriptor> m = new HashMap<>();
        m.put("toString", new PrototypePropertyDescriptor(toStringFunction, Noop));
        return m;
    }

}
