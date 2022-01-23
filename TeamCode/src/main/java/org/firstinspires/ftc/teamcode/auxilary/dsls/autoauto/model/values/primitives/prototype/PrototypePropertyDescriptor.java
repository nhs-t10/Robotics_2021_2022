package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoCallableValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;

public class PrototypePropertyDescriptor {
    public AutoautoCallableValue getter;
    public AutoautoCallableValue setter;

    public AutoautoPrimitive value;

    public PrototypePropertyDescriptor(AutoautoPrimitive value) {
        this.value = value;
    }
    public PrototypePropertyDescriptor(AutoautoCallableValue getter, AutoautoCallableValue setter) {
        this.getter = getter;
        this.setter = setter;
    }
}
