package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.table;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.PrototypePropertyDescriptor;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.universal.NoopFunction;

import java.util.HashMap;

public class TablePrototype {
    private static NoopFunction Noop = new NoopFunction();

    private static HashMap<String, PrototypePropertyDescriptor> map = null;

    public static HashMap<String, PrototypePropertyDescriptor> getMap() {
        if(map == null) initMap();
        return map;
    }

    private static void initMap() {
        map = new HashMap<>();
        //length has no setter
        map.put("length", new PrototypePropertyDescriptor(new LengthGetter(),null));

        map.put("push", new PrototypePropertyDescriptor(new PushFunction()));
    }
}
