package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.table;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.PrototypePropertyDescriptor;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.universal.NoopFunction;

import java.util.HashMap;

public class TablePrototype {
    private static NoopFunction Noop = new NoopFunction();
    private static LengthGetter lengthGetter = new LengthGetter();
    private static PushFunctionGetter pushGetter = new PushFunctionGetter();

    public static HashMap<String, PrototypePropertyDescriptor> getMap() {
        HashMap<String, PrototypePropertyDescriptor> m = new HashMap<>();
        m.put("length", new PrototypePropertyDescriptor(lengthGetter, Noop));
        m.put("push", new PrototypePropertyDescriptor(pushGetter, Noop));
        return m;
    }
}
