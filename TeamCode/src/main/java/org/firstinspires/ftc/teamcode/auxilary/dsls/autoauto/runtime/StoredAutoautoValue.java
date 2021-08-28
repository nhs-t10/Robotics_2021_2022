package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;

public class StoredAutoautoValue {
    public AutoautoPrimitive value;
    public boolean readOnly;
    public boolean systemManaged;
    
    public StoredAutoautoValue(AutoautoPrimitive v) {
        this.value = v;
    }
}
